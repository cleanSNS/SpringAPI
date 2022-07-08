package cleanbook.com.repository.page;

import cleanbook.com.domain.page.*;
import cleanbook.com.domain.user.QUser;
import cleanbook.com.domain.user.UserDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cleanbook.com.domain.page.QComment.comment;
import static cleanbook.com.domain.page.QPage.page;
import static cleanbook.com.domain.page.QPageImgUrl.pageImgUrl;
import static cleanbook.com.domain.user.QUser.user;

@Repository
@AllArgsConstructor
public class PageRepositoryImpl implements PageRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PageDetailDto readPageDetail(Long pageId) {
        return new PageDetailDto(readPageDto(pageId), readPageImgUrlList(pageId), readPageCommentList(pageId));
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

    public List<CommentDto> readPageCommentList(Long pageId) {
        return queryFactory.query()
                .select(Projections.constructor(CommentDto.class,
                            Projections.constructor(UserDto.class,
                                user.id, user.userProfile.nickname, user.userProfile.imgUrl),
                            comment.id, comment.content, comment.likeCount, comment.createdDate))
                .from(comment)
                .join(comment.user, user)
                .where(comment.page.id.eq(pageId))
                .fetch();
    }
}





