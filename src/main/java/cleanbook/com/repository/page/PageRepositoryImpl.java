package cleanbook.com.repository.page;

import cleanbook.com.domain.ResultDto;
import cleanbook.com.domain.page.*;
import cleanbook.com.domain.user.UserDto;
import cleanbook.com.exception.exceptions.NoMorePageException;
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

import static cleanbook.com.domain.page.QComment.comment;
import static cleanbook.com.domain.page.QPage.page;
import static cleanbook.com.domain.page.QPageImgUrl.pageImgUrl;
import static cleanbook.com.domain.user.QFollow.follow;
import static cleanbook.com.domain.user.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@AllArgsConstructor
public class PageRepositoryImpl implements PageRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    // 게시글 상세보기
    public PageDetailDto readPageDetail(Long pageId) {
        return new PageDetailDto(readPageDto(pageId), readPageImgUrlList(pageId), readPageCommentList(pageId, PageRequest.of(0,10)).getData());
    }

    public PageDto readPageDto(Long pageId) {
        return queryFactory.query()
                .select(Projections.constructor(PageDto.class,
                            Projections.constructor(UserDto.class,
                                user.id, user.userProfile.nickname, user.userProfile.imgUrl),
                            page.id, page.title, page.content, page.likeCount, page.createdDate))
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

    public ResultDto<List<CommentDto>> readPageCommentList(Long pageId, Pageable pageable) {
        List<CommentDto> commentDtoList = queryFactory.query()
                .select(Projections.constructor(CommentDto.class,
                        Projections.constructor(UserDto.class,
                                user.id, user.userProfile.nickname, user.userProfile.imgUrl),
                        comment.id, comment.content, comment.likeCount, comment.createdDate))
                .from(comment)
                .join(comment.user, user)
                .where(comment.page.id.eq(pageId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new ResultDto<>(commentDtoList, pageable.getPageNumber());
    }

    // 메인페이지 게시글 조회(내가 팔로우 한 사람만, 시간순)
    public ResultDto<List<MainPageDto>> readFolloweePageList(Long userId, Long startPageId, int pageSize) {

        // no offset방식
        // 페이지 pk 조회 및 페이징
        List<Long> pageIdList = queryFactory.query()
                .select(page.id)
                .from(follow)
                .join(follow.targetUser, user)
                .join(user.pageList, page)
                .where(follow.user.id.eq(userId), loePageId(startPageId))
                .orderBy(page.id.desc())
                .limit(pageSize)
                .fetch();

        // 조회를 전부 완료했을때
        if (pageIdList.isEmpty()) throw new NoMorePageException();

        List<MainPageDto> pageAndImgDtoList = new ArrayList<>();
        for (Long pageId : pageIdList) {
            pageAndImgDtoList.add(new MainPageDto(readPageDto(pageId), readPageImgUrlList(pageId)));
        }
        Long nextStartPageId = pageIdList.stream().mapToLong(x->x).min().getAsLong()-1;

        return new ResultDto<>(pageAndImgDtoList, nextStartPageId);
    }

    // 특정 유저 게시글 전체조회(유저페이지)
    // dto로 바로 조회
//    public ResultDto<List<UserPageDto>> readUserPageList(Long userId, Long startPageId, int pageSize) {
//
//        // no offset방식
//        // 페이지 pk 조회 및 페이징
//        List<Long> pageIdList = queryFactory.query()
//                .select(page.id)
//                .from(page)
//                .where(page.user.id.eq(userId), loePageId(startPageId))
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
//        return new ResultDto<>(userPageDtoList, getNextPageId(userId, startPageId, pageSize));
//    }

    // 특정 유저 게시글 전체조회(유저페이지)
    // 엔티티로 조회 후 dto로 변환
    public ResultDto<List<UserPageDto>> readUserPageList(Long userId, Long startPageId, int pageSize) {

        // no offset방식
        // 페이지 pk 조회 및 페이징
        List<Page> pageList = queryFactory.query()
                .select(page)
                .from(page)
                .where(page.user.id.eq(userId), loePageId(startPageId))
                .orderBy(page.id.desc())
                .limit(pageSize)
                .fetch();

        // 조회를 전부 완료했을때
        if (pageList.isEmpty()) throw new NoMorePageException();

        List<UserPageDto> userPageDtoList = pageList.stream().map(
                        p -> new UserPageDto(p.getId(), p.getTitle(), p.getLikeCount(),
                                p.getImgUrlList().stream().map(u -> u.getImgUrl()).collect(Collectors.toList())))
                .collect(Collectors.toList());

        return new ResultDto<>(userPageDtoList, getNextPageId(userId, startPageId, pageSize));
    }

    private Long getNextPageId(Long userId, Long pageId, int pageSize) {
        Long nextPageId = pageId;
        if (pageId == null) {
            nextPageId = getMaxPageId(userId);
        }
        nextPageId -= pageSize;
        System.out.println("nextPageId = " + nextPageId);

        return nextPageId;
    }

    private Long getMaxPageId(Long userId) {
        Long count = queryFactory.query()
                .select(page.id.max())
                .from(page)
                .where(page.user.id.eq(userId))
                .fetchOne();
        System.out.println("pageCount = " + count);
        return count;
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





