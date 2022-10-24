package cleanbook.com.repository.comment;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.CommentDto;
import cleanbook.com.dto.page.UserPageDto;
import cleanbook.com.dto.user.UserDto;
import cleanbook.com.entity.page.Comment;
import cleanbook.com.entity.page.QComment;
import cleanbook.com.entity.user.like.LikeComment;
import cleanbook.com.entity.user.like.QLikeComment;
import cleanbook.com.exception.exceptions.NoMoreCommentException;
import cleanbook.com.repository.user.like.LikeCommentRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cleanbook.com.entity.page.QComment.comment;
import static cleanbook.com.entity.page.QPage.page;
import static cleanbook.com.entity.user.QUser.user;
import static cleanbook.com.entity.user.like.QLikeComment.likeComment;
import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@AllArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final LikeCommentRepository likeCommentRepository;

    public boolean isLikeComment(Long userId, Long commentId) {
        return likeCommentRepository.findByComment_IdAndUser_Id(commentId,userId).isPresent();
    }

    // 댓글 조회
    public ResultDto<List<CommentDto>> readCommentList(Long userId, Long pageId, Long startId, int pageSize) {

        // nested false인 댓글을 10개씩
        List<CommentDto> commentDtoList = queryFactory.query()
                .select(Projections.constructor(CommentDto.class,
                        Projections.constructor(UserDto.class,
                                user.id, user.userProfile.nickname, user.userProfile.imgUrl),
                        comment.id, comment.content, comment.group, comment.likeCount, comment.createdDate
                ))
                .from(comment)
                .join(comment.user, user)
                .where(comment.page.id.eq(pageId), comment.nested.eq(false), goeCommentId(startId))
                .limit(pageSize)
                .orderBy(comment.id.asc())
                .transform(
                        groupBy(comment.id).list(
                                Projections.constructor(CommentDto.class,
                                    Projections.constructor(UserDto.class,
                                            user.id, user.userProfile.nickname, user.userProfile.imgUrl),
                                    comment.id, comment.content, comment.group, comment.likeCount, comment.createdDate
                                ))
                );

        if (commentDtoList.isEmpty()) {
            return new ResultDto<>(new ArrayList<>(), 0L);
        }

        for (CommentDto commentDto : commentDtoList) {
            commentDto.setLike(isLikeComment(userId, commentDto.getCommentId()));
        }
        Long nextStartId = commentDtoList.get(commentDtoList.size()-1).getCommentId() + 1;

        return new ResultDto<>(commentDtoList, nextStartId);
    }

    private BooleanExpression goeCommentId(Long commentId) {
        return commentId == null ? null : comment.id.goe(commentId);
    }

    // 댓글 조회
    public ResultDto<List<CommentDto>> readNestedCommentList(Long userId, Long pageId, int group, Long startId, int pageSize) {

        // nested false인 댓글을 10개씩
        List<CommentDto> commentDtoList = queryFactory.query()
                .select(Projections.constructor(CommentDto.class,
                        Projections.constructor(UserDto.class,
                                user.id, user.userProfile.nickname, user.userProfile.imgUrl),
                        comment.id, comment.content, comment.group, comment.likeCount, comment.createdDate
                ))
                .from(comment)
                .join(comment.user, user)
                .where(comment.page.id.eq(pageId), comment.group.eq(group), comment.nested.eq(true), goeCommentId(startId))
                .limit(pageSize)
                .orderBy(comment.id.asc())
                .transform(
                        groupBy(comment.id).list(
                                Projections.constructor(CommentDto.class,
                                        Projections.constructor(UserDto.class,
                                                user.id, user.userProfile.nickname, user.userProfile.imgUrl),
                                        comment.id, comment.content, comment.group, comment.likeCount, comment.createdDate
                                ))
                );

        if (commentDtoList.isEmpty()) {
            return new ResultDto<>(new ArrayList<>(), 0L);
        }

        for (CommentDto commentDto : commentDtoList) {
            commentDto.setLike(isLikeComment(userId, commentDto.getCommentId()));
        }
        Long nextStartId = commentDtoList.get(commentDtoList.size()-1).getCommentId() + 1;

        return new ResultDto<>(commentDtoList, nextStartId);
    }
}
