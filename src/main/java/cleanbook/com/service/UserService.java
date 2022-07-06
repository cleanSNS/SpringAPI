package cleanbook.com.service;

import cleanbook.com.domain.page.Comment;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.user.*;
import cleanbook.com.domain.user.like.LikeComment;
import cleanbook.com.domain.user.like.LikePage;
import cleanbook.com.domain.user.like.LikeType;
import cleanbook.com.domain.user.report.ReportComment;
import cleanbook.com.domain.user.report.ReportPage;
import cleanbook.com.domain.user.report.ReportType;
import cleanbook.com.domain.user.report.ReportUser;
import cleanbook.com.exception.CommentNotFoundException;
import cleanbook.com.exception.PageNotFoundException;
import cleanbook.com.exception.UserNotFoundException;
import cleanbook.com.repository.*;
import cleanbook.com.repository.user.*;
import cleanbook.com.repository.user.report.ReportCommentRepository;
import cleanbook.com.repository.user.report.ReportPageRepository;
import cleanbook.com.repository.user.report.ReportUserRepository;
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
    private final ReportUserRepository reportUserRepository;
    private final ReportPageRepository reportPageRepository;
    private final ReportCommentRepository reportCommentRepository;

    // 팔로우하기
    public Long followUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);

        Follow follow = new Follow(user, targetUser);
        followRepository.save(follow);
        return follow.getId();
    }

    // 좋아요
    public void likeComment(Long userId, Long targetId, LikeType type) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        switch (type) {
            case PAGE:
                Page page = pageRepository.findById(targetId).orElseThrow(PageNotFoundException::new);
                likePageRepository.save(new LikePage(user, page));

            case COMMENT:
                Comment comment = commentRepository.findById(targetId).orElseThrow(CommentNotFoundException::new);
                likeCommentRepository.save(new LikeComment(user, comment));
        }
    }

    // 신고
    public void report(Long userId, Long targetId, ReportType type) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        switch (type){
            case USER:
                User targetUser = userRepository.findById(targetId).orElseThrow(UserNotFoundException::new);
                reportUserRepository.save(new ReportUser(user, targetUser));

            case PAGE:
                Page targetPage = pageRepository.findById(targetId).orElseThrow(PageNotFoundException::new);
                reportPageRepository.save(new ReportPage(user, targetPage));

            case COMMENT:
                Comment targetComment = commentRepository.findById(targetId).orElseThrow(CommentNotFoundException::new);
                reportCommentRepository.save(new ReportComment(user, targetComment));

        }
    }
}
