package cleanbook.com.repository.page;

import cleanbook.com.dto.CountDto;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.*;
import cleanbook.com.entity.enums.SettingType;
import cleanbook.com.entity.page.*;
import cleanbook.com.dto.user.UserDto;
import cleanbook.com.entity.user.follow.QFollow;
import cleanbook.com.repository.FollowRepository;
import cleanbook.com.repository.comment.CommentRepository;
import cleanbook.com.repository.user.like.LikePageRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cleanbook.com.entity.page.QHashtag.hashtag;
import static cleanbook.com.entity.page.QPage.page;
import static cleanbook.com.entity.page.QPageHashtag.pageHashtag;
import static cleanbook.com.entity.page.QPageImgUrl.pageImgUrl;
import static cleanbook.com.entity.user.follow.QFollow.follow;
import static cleanbook.com.entity.user.QUser.user;

@Repository
@AllArgsConstructor
public class PageRepositoryImpl implements PageRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;
    private final LikePageRepository likePageRepository;

    // 게시글 상세보기
    public PageDetailDto readPageDetail(Long userId, Long pageId) {
        return new PageDetailDto(readPageDto(pageId), readPageImgUrlList(pageId), readPageHashtagList(pageId), readPageCommentList(userId, pageId), isLikePage(userId, pageId));
    }

    public PageDto readPageDto(Long pageId) {
        return queryFactory.query()
                .select(Projections.constructor(PageDto.class,
                            Projections.constructor(UserDto.class,
                                user.id, user.userProfile.nickname, user.userProfile.imgUrl),
                            page.id, page.content, page.likeCount, page.pageSetting.likeReadAuth, page.createdDate))
                .from(page)
                .join(page.user, user)
                .where(page.id.eq(pageId))
                .fetchOne();
    }

    public List<String> readPageImgUrlList(Long pageId) {
        return queryFactory.query()
                .select(pageImgUrl.imgUrl)
                .from(page)
                .join(page.pageImgUrlList, pageImgUrl)
                .where(page.id.eq(pageId))
                .fetch();
    }

    public List<String> readPageHashtagList(Long pageId) {
        return queryFactory.query()
                .select(pageHashtag.hashtag.name)
                .from(page)
                .join(page.pageHashtagList, pageHashtag)
                .where(page.id.eq(pageId))
                .fetch();
    }

    public ResultDto<List<CommentDto>> readPageCommentList(Long userId, Long pageId) {
        return commentRepository.readCommentList(userId, pageId, 1L, 10);
    }

    public boolean isLikePage(Long userId, Long pageId) {
        return likePageRepository.findByPage_IdAndUser_Id(pageId, userId).isPresent();
    }

    // 메인페이지 게시글 조회(내가 팔로우 한 사람만, 시간순)
    // FOLLOW_ONLY일 경우 내가 내 게시글 보려는사람을 팔로우햇을 경우에만 보임
    // 팔로우 x경우 ALL만
    // 팔로우 o경우 ALL, FOLLOW_ONLY
    public ResultDto<List<MainPageDto>> readFolloweePageList(Long userId, Long startId, int pageSize) {

        QFollow follow2 = new QFollow("follow2");

        // no offset방식
        // 페이지 pk 조회 및 페이징
        List<Long> pageIdList = queryFactory.query()
                .select(page.id).distinct()
                .from(follow)
                .join(follow.targetUser, user)
                .join(user.pageList, page)
                .leftJoin(page.user.followeeList, follow2)
                .where(follow.user.id.eq(userId), loePageId(startId), readAuth(userId, follow2))
                .orderBy(page.id.desc())
                .limit(pageSize)
                .fetch();

        // 조회를 전부 완료했을때
        if (pageIdList.isEmpty()) {
            return new ResultDto<>(new ArrayList<>(), 0L);
        }

        List<MainPageDto> pageAndImgDtoList = new ArrayList<>();
        for (Long pageId : pageIdList) {
            pageAndImgDtoList.add(new MainPageDto(readPageDto(pageId), readPageImgUrlList(pageId), isLikePage(userId, pageId)));
        }
        Long nextStartId = pageIdList.stream().mapToLong(x->x).min().getAsLong()-1;

        return new ResultDto<>(pageAndImgDtoList, nextStartId);
    }

    private BooleanExpression readAuth(Long userId, QFollow follow2) {
        return page.pageSetting.readAuth.eq(SettingType.ALL)
        .or(page.pageSetting.readAuth.eq(SettingType.FOLLOW_ONLY).and(follow2.user.id.eq(user.id)).and(follow2.targetUser.id.eq(userId)));
    }

    // 특정 유저 게시글 전체조회(유저페이지)
    // dto로 바로 조회
//    public ResultDto<List<UserPageDto>> readUserPageList(Long userId, Long startId, int pageSize) {
//
//        // no offset방식
//        // 페이지 pk 조회 및 페이징
//        List<Long> pageIdList = queryFactory.query()
//                .select(page.id)
//                .from(page)
//                .where(page.user.id.eq(userId), loePageId(startId))
//                .orderBy(page.id.desc())
//                .limit(pageSize)
//                .fetch();
//
//        for (Long aLong : pageIdList) {
//            System.out.println("aLong = " + aLong);
//        }
//
//        // 조회를 전부 완료했을때
//        if (pageIdList.isEmpty()) throw new NoMorePageException();
//
//        List<UserPageDto> userPageDtoList = queryFactory.query()
//                .select(page.id, page.title, page.likeCount, pageImgUrl.imgUrl)
//                .from(page)
//                .leftJoin(page.imgUrlList, pageImgUrl)
//                .where(page.id.in(pageIdList))
//                .orderBy(page.id.desc())
//                .transform(
//                        groupBy(page.id).list(
//                                Projections.constructor(UserPageDto.class,
//                                        page.id, page.title, page.likeCount,
//                                        list(pageImgUrl.imgUrl))
//                        )
//                );
//
//        for (UserPageDto userPageDto : userPageDtoList) {
//            System.out.println("userPageDto.getPageId() = " + userPageDto.getPageId());
//        }
//
//
//        return new ResultDto<>(userPageDtoList, getNextPageId(userId, startId, pageSize));
//    }

    // 특정 유저 게시글 전체조회(유저페이지)
    // 엔티티로 조회 후 dto로 변환
    // ALL - 전체
    // FOLLOW_ONLY 맞팔
    // NONE - 자신만
    public ResultDto<List<UserPageDto>> readUserPageList(Long userId, Long targetUserId, Long startId, int pageSize) {

        // no offset방식
        // 페이지 pk 조회 및 페이징
        List<Page> pageList = queryFactory.query()
                .select(page).distinct()
                .from(page)
                .join(page.user, user)
                .leftJoin(user.followeeList, follow)
                .where(page.user.id.eq(targetUserId), loePageId(startId),userPageReadAuth(userId, targetUserId))
                .orderBy(page.id.desc())
                .limit(pageSize)
                .fetch();

        // 조회를 전부 완료했을때
        if (pageList.isEmpty()) {
            return new ResultDto<>(new ArrayList<>(), 0L);
        }

        List<UserPageDto> userPageDtoList = pageList.stream()
                    .map(p -> new UserPageDto(p.getId(), p.getLikeCount(), p.getPageSetting().getLikeReadAuth(),
                                p.getPageImgUrlList().get(0).getImgUrl()))
                    .collect(Collectors.toList());

        Long nextStartId = pageList.stream()
                .map(p -> p.getId())
                .mapToLong(x -> x).min().getAsLong() - 1;

        return new ResultDto<>(userPageDtoList, nextStartId);
    }

    private BooleanExpression loePageId(Long pageId) {
        return pageId == null ? null : page.id.loe(pageId);
    }

    private BooleanExpression userPageReadAuth(Long userId, Long targetUserId) {
        return page.pageSetting.readAuth.eq(SettingType.ALL)
                .or(page.pageSetting.readAuth.eq(SettingType.FOLLOW_ONLY).and((follow.user.id.eq(targetUserId)).and(follow.targetUser.id.eq(userId)).or(page.user.id.eq(userId))))
                .or(page.pageSetting.readAuth.eq(SettingType.NONE).and(page.user.id.eq(userId)));
    }

    // 해시태그로 페이지 검색
    public ResultDto<List<UserPageDto>> readPageByHashtag(String hashtagName, Long startId, int pageSize) {
        List<Page> pageList = queryFactory.query()
                .select(page)
                .from(page)
                .leftJoin(page.pageHashtagList, pageHashtag)
                .join(pageHashtag.hashtag, hashtag)
                .where(hashtag.name.eq(hashtagName), loePageId(startId))
                .orderBy(page.id.desc())
                .limit(pageSize)
                .fetch();

        // 조회를 전부 완료했을때
        if (pageList.isEmpty()) {
            return new ResultDto<>(new ArrayList<>(), 0L);
        }

        List<UserPageDto> userPageDtoList = pageList.stream()
                        .map(p -> new UserPageDto(p.getId(), p.getLikeCount(), p.getPageSetting().getLikeReadAuth(),
                                p.getPageImgUrlList().get(0).getImgUrl()))
                        .collect(Collectors.toList());

        Long nextStartId = pageList.stream()
                .map(p -> p.getId())
                .mapToLong(x -> x).min().getAsLong() - 1;

        return new ResultDto<>(userPageDtoList, nextStartId);
    }

    public ResultDto<CountDto> getPageListCountByHashtag(String hashtagName) {
        Long count = queryFactory.query()
                .select(page.count())
                .from(page)
                .leftJoin(page.pageHashtagList, pageHashtag)
                .join(pageHashtag.hashtag, hashtag)
                .where(hashtag.name.eq(hashtagName))
                .fetchOne();

        return new ResultDto<>(new CountDto(count));
    }


    // 테스트용 메서드
    public void testQuery(Long userId) {
        List<Page> pageList = queryFactory.query()
                .select(page)
                .from(follow)
                .join(follow.targetUser, user)
                .join(user.pageList, page)
                .where(follow.user.id.eq(userId))
                .fetch();
        for (Page page : pageList) {
            System.out.println("userId = " + page.getUser().getId());
        }
    }
}





