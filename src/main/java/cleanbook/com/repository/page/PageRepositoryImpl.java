package cleanbook.com.repository.page;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.*;
import cleanbook.com.entity.page.*;
import cleanbook.com.dto.user.UserDto;
import cleanbook.com.exception.exceptions.NoMorePageException;
import cleanbook.com.repository.comment.CommentRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cleanbook.com.entity.page.QComment.comment;
import static cleanbook.com.entity.page.QPage.page;
import static cleanbook.com.entity.page.QPageHashtag.pageHashtag;
import static cleanbook.com.entity.page.QPageImgUrl.pageImgUrl;
import static cleanbook.com.entity.user.follow.QFollow.follow;
import static cleanbook.com.entity.user.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@AllArgsConstructor
public class PageRepositoryImpl implements PageRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final CommentRepository commentRepository;

    // 게시글 상세보기
    public PageDetailDto readPageDetail(Long pageId) {
        return new PageDetailDto(readPageDto(pageId), readPageImgUrlList(pageId), readPageHashtagList(pageId), readPageCommentList(pageId));
    }

    public PageDto readPageDto(Long pageId) {
        return queryFactory.query()
                .select(Projections.constructor(PageDto.class,
                            Projections.constructor(UserDto.class,
                                user.id, user.userProfile.nickname, user.userProfile.imgUrl),
                            page.id, page.content, page.likeCount, page.createdDate))
                .from(page)
                .join(page.user, user)
                .where(page.id.eq(pageId))
                .fetchOne();
    }

    public List<String> readPageImgUrlList(Long pageId) {
        return queryFactory.query()
                .select(pageImgUrl.imgUrl)
                .from(page)
                .join(page.imgUrlList, pageImgUrl)
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

    public ResultDto<List<CommentDto>> readPageCommentList(Long pageId) {
        return commentRepository.readCommentList(pageId, 1L, 10);
    }

    // 메인페이지 게시글 조회(내가 팔로우 한 사람만, 시간순)
    public ResultDto<List<MainPageDto>> readFolloweePageList(Long userId, Long startId, int pageSize) {

        // no offset방식
        // 페이지 pk 조회 및 페이징
        List<Long> pageIdList = queryFactory.query()
                .select(page.id)
                .from(follow)
                .join(follow.targetUser, user)
                .join(user.pageList, page)
                .where(follow.user.id.eq(userId), loePageId(startId))
                .orderBy(page.id.desc())
                .limit(pageSize)
                .fetch();

        // 조회를 전부 완료했을때
        if (pageIdList.isEmpty()) throw new NoMorePageException();

        List<MainPageDto> pageAndImgDtoList = new ArrayList<>();
        for (Long pageId : pageIdList) {
            pageAndImgDtoList.add(new MainPageDto(readPageDto(pageId), readPageImgUrlList(pageId)));
        }
        Long nextStartId = pageIdList.stream().mapToLong(x->x).min().getAsLong()-1;

        return new ResultDto<>(pageAndImgDtoList, nextStartId);
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
    public ResultDto<List<UserPageDto>> readUserPageList(Long userId, Long startId, int pageSize) {

        // no offset방식
        // 페이지 pk 조회 및 페이징
        List<Page> pageList = queryFactory.query()
                .select(page)
                .from(page)
                .where(page.user.id.eq(userId), loePageId(startId))
                .orderBy(page.id.desc())
                .limit(pageSize)
                .fetch();

        // 조회를 전부 완료했을때
        if (pageList.isEmpty()) throw new NoMorePageException();

        List<UserPageDto> userPageDtoList = pageList.stream().map(
                        p -> new UserPageDto(p.getId(), p.getContent(), p.getLikeCount(),
                                p.getImgUrlList().stream().map(u -> u.getImgUrl()).collect(Collectors.toList())))
                .collect(Collectors.toList());

        Long nextStartId = pageList.stream()
                .map(p -> p.getId())
                .mapToLong(x -> x).min().getAsLong() - 1;

        return new ResultDto<>(userPageDtoList, nextStartId);
    }

    private BooleanExpression loePageId(Long pageId) {
        return pageId == null ? null : page.id.loe(pageId);
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





