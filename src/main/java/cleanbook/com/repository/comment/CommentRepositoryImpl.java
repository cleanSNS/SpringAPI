package cleanbook.com.repository.comment;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.CommentDto;
import cleanbook.com.dto.user.UserDto;
import cleanbook.com.entity.page.Comment;
import cleanbook.com.entity.page.QComment;
import cleanbook.com.entity.user.like.LikeComment;
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
        List<Comment> commentList = queryFactory.query()
                .select(comment)
                .from(comment)
                .where(comment.page.id.eq(pageId), comment.nested.eq(false), goeCommentId(startId))
                .limit(pageSize)
                .orderBy(comment.id.asc())
                .fetch();

        if (commentList.isEmpty()) {
            return new ResultDto<>(new ArrayList<>(), 0L);
        }

        List<CommentDto> commentDtoList = commentList.stream()
                .map(c -> new CommentDto(new UserDto(c.getUser().getId(), c.getUser().getUserProfile().getNickname(), c.getUser().getUserProfile().getImgUrl())
                        , c.getId(), c.getContent(), c.getFilteredContent(), c.getGroup(), c.getLikeCount(), isLikeComment(userId, c.getId()) , c.getCreatedDate()))
                .collect(Collectors.toList());

        Long nextStartId = commentList.get(commentList.size()-1).getId() + 1;

        return new ResultDto<>(commentDtoList, nextStartId);
    }

    private BooleanExpression goeCommentId(Long commentId) {
        return commentId == null ? null : comment.id.goe(commentId);
    }

    // 대댓글 조회
    public ResultDto<List<CommentDto>> readNestedCommentList(Long userId, Long pageId, int group, Long startId, int pageSize) {

        List<Comment> commentList = queryFactory.query()
                .select(comment)
                .from(comment)
                .where(comment.page.id.eq(pageId), comment.group.eq(group), comment.nested.eq(true), goeCommentId(startId))
                .limit(pageSize)
                .orderBy(comment.order.asc())
                .fetch();

        if (commentList.isEmpty()) {
            return new ResultDto<>(new ArrayList<>(), 0L);
        }

        List<CommentDto> commentDtoList = commentList.stream()
                .map(c -> new CommentDto(new UserDto(c.getUser().getId(), c.getUser().getUserProfile().getNickname(), c.getUser().getUserProfile().getImgUrl())
                        , c.getId(), c.getContent(), c.getFilteredContent(), c.getGroup(), c.getLikeCount(), isLikeComment(userId, c.getId()), c.getCreatedDate()))
                .collect(Collectors.toList());

        Long nextStartId = commentList.get(commentList.size()-1).getId() + 1;

        return new ResultDto<>(commentDtoList, nextStartId);
    }
}
