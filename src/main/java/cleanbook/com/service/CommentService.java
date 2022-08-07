package cleanbook.com.service;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.CommentCreateDto;
import cleanbook.com.dto.page.CommentDto;
import cleanbook.com.entity.notification.NotificationType;
import cleanbook.com.entity.page.Comment;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.CommentNotFoundException;
import cleanbook.com.exception.exceptions.NoAuthroizationException;
import cleanbook.com.exception.exceptions.PageNotFoundException;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.repository.comment.CommentRepository;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PageRepository pageRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    // 댓글 생성
    public void createComment(Long userId, CommentCreateDto dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Page page = pageRepository.findById(dto.getPageId()).orElseThrow(PageNotFoundException::new);
        User targetUser = userRepository.findById(page.getUser().getId()).orElseThrow(UserNotFoundException::new);
        Comment comment = Comment.createComment(user, page, dto.getContent(), dto.getGroup(), dto.isNested(), dto.isVisible());

        if (comment.isNested()) {
            int order = commentRepository.findFirstByGroupOrderByOrderDesc(dto.getGroup()).orElseThrow(CommentNotFoundException::new).getOrder();
            comment.setOrder(order+1);
        }

        commentRepository.save(comment);
        notificationService.send(user, targetUser, dto.isNested() ? NotificationType.NESTED : NotificationType.COMMENT);
    }

    // 댓글 조회(한 게시글의 댓글 전체 조회, 대댓글 제외, 10개씩)
    public ResultDto<List<CommentDto>> readCommentList(Long pageId, Long startId) {
        return commentRepository.readCommentList(pageId, startId, 10);
    }

    // 대댓글 조회(한 댓글의 댓글 조회, 10개씩)
    public ResultDto<List<CommentDto>> readNestedCommentList(Long pageId, int group, Long startId) {
        return commentRepository.readNestedCommentList(pageId, group, startId, 10);
    }

    // 댓글 삭제
    public void deleteComment(Long userId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if (!comment.getUser().getId().equals(userId)) {
            throw new NoAuthroizationException();
        }

        commentRepository.delete(comment);
    }
}
