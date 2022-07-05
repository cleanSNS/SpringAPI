package cleanbook.com.service;

import cleanbook.com.domain.page.Comment;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.user.*;
import cleanbook.com.domain.user.like.LikeComment;
import cleanbook.com.domain.user.like.LikePage;
import cleanbook.com.domain.user.report.ReportType;
import cleanbook.com.exception.CommentNotFoundException;
import cleanbook.com.exception.PageNotFoundException;
import cleanbook.com.exception.UserNotFoundException;
import cleanbook.com.repository.*;
import cleanbook.com.repository.user.LikeCommentRepository;
import cleanbook.com.repository.user.LikePageRepository;
import cleanbook.com.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final PageRepository pageRepository;
    private final LikePageRepository likePageRepository;

    // 팔로우하기
    public Long followUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);

        Follow follow = new Follow(user, targetUser);
        followRepository.save(follow);
        return follow.getId();
    }

    // 댓글 좋아요
    public void likeComment(Long userId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        likeCommentRepository.save(new LikeComment(user, comment));
    }

    // 글 좋아요
    public void likePage(Long userId, Long pageId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFoundException::new);
        likePageRepository.save(new LikePage(user, page));
    }

    // 유저 신고
    public void report(Long userId, ReportType type, Long targetId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        switch (type){
            case USER:
                User targetUser = userRepository.findById(targetId).orElseThrow(UserNotFoundException::new);


        }
    }
}
