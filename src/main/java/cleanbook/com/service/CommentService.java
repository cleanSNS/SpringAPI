package cleanbook.com.service;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.CommentCreateDto;
import cleanbook.com.dto.page.CommentDto;
import cleanbook.com.entity.notification.Notification;
import cleanbook.com.entity.notification.NotificationType;
import cleanbook.com.entity.page.Comment;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.*;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.repository.comment.CommentRepository;
import cleanbook.com.repository.notification.NotificationRepository;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static cleanbook.com.entity.notification.Notification.createNotification;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PageRepository pageRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

    // 댓글 생성
    public void createComment(Long userId, CommentCreateDto dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Page page = pageRepository.findById(dto.getPageId()).orElseThrow(PageNotFoundException::new);
        User targetUser = userRepository.findById(page.getUser().getId()).orElseThrow(UserNotFoundException::new);
        Comment comment = Comment.createComment(user, page, dto.getContent(), dto.getGroup(), dto.isNested(), dto.isVisible());

        // 댓글 권한이 없는 게시글
        if (!page.getPageSetting().isCommentAuth()) {
            throw new MyException("댓글을 달 수 없는 게시글입니다.");
        }

        if (comment.isNested()) { // 대댓글일시
            int order = commentRepository.findFirstByPage_IdAndGroupOrderByOrderDesc(page.getId(), dto.getGroup()).orElseThrow(CommentNotFoundException::new).getOrder();
            comment.setOrder(order+1);

            // 본인이 작성한 글이 아니고 댓글 알림 허용했을 경우 알림 발송
            // todo SSE 알림+1
            if (!userId.equals(page.getUser().getId()) && page.getPageSetting().isNotificationComment()) {
                Comment headComment = commentRepository.findFirstByPage_IdAndGroupOrderByOrderAsc(page.getId(), dto.getGroup()).orElseThrow(CommentNotFoundException::new);
                notificationRepository.save(createNotification(user, targetUser,NotificationType.NESTED, headComment.getId()));
            }
        } else { // 댓글일시
            // 본인이 작성한 글이 아니고 댓글 알림 허용했을 경우 알림 발송
            // todo SSE 알림+1

            // 그룹 설정
            Optional<Comment> optional = commentRepository.findFirstByPage_IdOrderByGroupDesc(page.getId());
            if (optional.isPresent()) {
                comment.changeGroup(optional.get().getGroup()+1);
            } else { // 댓글이 없을 경우 -> 첫 댓글은 그룹 1
                comment.changeGroup(1);
            }

            if (!userId.equals(page.getUser().getId()) && page.getPageSetting().isNotificationComment()) {
                notificationRepository.save(createNotification(user, targetUser,NotificationType.COMMENT, comment.getId()));
            }
        }

        commentRepository.save(comment);

    }

    // 댓글 조회(한 게시글의 댓글 전체 조회, 대댓글 제외, 10개씩)
    public ResultDto<List<CommentDto>> readCommentList(Long pageId, Long startId) {
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFoundException::new);
        if (!page.getPageSetting().isCommentAuth()) {
            throw new MyException("댓글을 볼 수 없는 게시글입니다.");
        }
        return commentRepository.readCommentList(pageId, startId, 10);
    }

    // 대댓글 조회(한 댓글의 댓글 조회, 10개씩)
    public ResultDto<List<CommentDto>> readNestedCommentList(Long pageId, int group, Long startId) {
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFoundException::new);
        if (!page.getPageSetting().isCommentAuth()) {
            throw new MyException("댓글을 볼 수 없는 게시글입니다.");
        }
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
