package cleanbook.com.repository.comment;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.CommentDto;
import cleanbook.com.dto.page.NestedCommentDto;
import cleanbook.com.dto.page.UserPageDto;
import cleanbook.com.dto.user.UserDto;
import cleanbook.com.entity.page.Comment;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.page.QComment;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.like.LikeComment;
import cleanbook.com.entity.user.like.QLikeComment;
import cleanbook.com.exception.exceptions.NoMoreCommentException;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.FollowRepository;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.repository.user.like.LikeCommentRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final EntityManager em;

    public boolean isLikeComment(Long userId, Long commentId) {
        return likeCommentRepository.findByComment_IdAndUser_Id(commentId,userId).isPresent();
    }


    // 필터링 조건에 따라 댓글 내용 리턴
    public String getContent(Long userId, Comment comment) {
        if (userId == null) {
            return comment.getFilteredContent();
        }
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Boolean filterAll = user.getUserSetting().getUserFilterSetting().getFilterAll();
        Boolean filterFollowee = user.getUserSetting().getUserFilterSetting().getFilterFollowee();
        // 자신의 댓글
        if (userId.equals(comment.getUser().getId())) {
            return comment.getContent();
        }
        // 팔로우 관계
        if (followRepository.findByUser_IdAndTargetUser_Id(userId, comment.getUser().getId()).isPresent()) {
            if (filterFollowee) {
                return comment.getFilteredContent();
            } else {
                return comment.getContent();
            }
        } else { // 무관계
            if (filterAll) {
                return comment.getFilteredContent();
            } else {
                return comment.getContent();
            }
        }
    }

    // 댓글 조회
    public ResultDto<List<CommentDto>> readCommentList(Long userId, Long pageId, Long startId, int pageSize) {

        // nested false인 댓글을 10개씩
        List<Comment> commentList = queryFactory.query()
                .select(comment)
                .from(comment)
                .join(comment.user, user).fetchJoin()
                .where(comment.page.id.eq(pageId), comment.nested.eq(false), goeCommentId(startId))
                .limit(pageSize)
                .orderBy(comment.id.asc())
                .fetch();

        if (commentList.isEmpty()) {
            return new ResultDto<>(new ArrayList<>(), 0L);
        }

        List<CommentDto> commentDtoList = commentList.stream()
                .map(c -> new CommentDto(new UserDto(c.getUser().getId(), c.getUser().getUserProfile().getNickname(), c.getUser().getUserProfile().getImgUrl()),
                        c.getId(), getContent(c.getUser().getId(), c), c.getGroup(), c.getLikeCount(),
                        getNestCommentCount(pageId, c.getGroup()), isLikeComment(userId, c.getId()) , c.getCreatedDate()))
                .collect(Collectors.toList());

        Long nextStartId = commentList.get(commentList.size()-1).getId() + 1;

        return new ResultDto<>(commentDtoList, nextStartId);
    }

    private BooleanExpression goeCommentId(Long commentId) {
        return commentId == null ? null : comment.id.goe(commentId);
    }

    // 대댓글 개수 조회
    public int getNestCommentCount(Long pageId, int group) {
        return queryFactory.query()
                .select(comment.count())
                .from(comment)
                .where(comment.page.id.eq(pageId), comment.nested.eq(true), comment.group.eq(group))
                .fetchOne().intValue();
    }

    // 대댓글 조회
    public ResultDto<List<NestedCommentDto>> readNestedCommentList(Long userId, Long pageId, int group, Long startId, int pageSize) {

        List<Comment> commentList = queryFactory.query()
                .select(comment)
                .from(comment)
                .join(comment.user, user).fetchJoin()
                .where(comment.page.id.eq(pageId), comment.group.eq(group), comment.nested.eq(true), goeCommentId(startId))
                .limit(pageSize)
                .orderBy(comment.order.asc())
                .fetch();

        if (commentList.isEmpty()) {
            return new ResultDto<>(new ArrayList<>(), 0L);
        }

        List<NestedCommentDto> commentDtoList = commentList.stream()
                .map(c -> new NestedCommentDto(new UserDto(c.getUser().getId(), c.getUser().getUserProfile().getNickname(), c.getUser().getUserProfile().getImgUrl())
                        , c.getId(), getContent(c.getUser().getId(), c), c.getGroup(), c.getLikeCount(), isLikeComment(userId, c.getId()), c.getCreatedDate()))
                .collect(Collectors.toList());

        Long nextStartId = commentList.get(commentList.size()-1).getId() + 1;

        return new ResultDto<>(commentDtoList, nextStartId);
    }
}
