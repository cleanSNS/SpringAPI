package cleanbook.com.service;

import cleanbook.com.domain.page.Comment;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.user.*;
import cleanbook.com.domain.user.block.Block;
import cleanbook.com.domain.user.block.BlockedUserDto;
import cleanbook.com.domain.user.filter.Filter;
import cleanbook.com.domain.user.UserDto;
import cleanbook.com.domain.user.like.LikeComment;
import cleanbook.com.domain.user.like.LikePage;
import cleanbook.com.domain.user.like.LikeType;
import cleanbook.com.domain.user.report.ReportType;
import cleanbook.com.exception.CommentNotFoundException;
import cleanbook.com.exception.EmptyStringException;
import cleanbook.com.exception.PageNotFoundException;
import cleanbook.com.exception.UserNotFoundException;
import cleanbook.com.repository.*;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.*;
import cleanbook.com.repository.user.like.LikeCommentRepository;
import cleanbook.com.repository.user.like.LikePageRepository;
import cleanbook.com.repository.user.report.ReportCommentRepository;
import cleanbook.com.repository.user.report.ReportPageRepository;
import cleanbook.com.repository.user.report.ReportUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cleanbook.com.domain.user.filter.Filter.createFilter;
import static cleanbook.com.domain.user.block.Block.createBlock;
import static cleanbook.com.domain.user.report.ReportComment.createReportComment;
import static cleanbook.com.domain.user.report.ReportPage.createReportPage;
import static cleanbook.com.domain.user.report.ReportUser.createReportUser;
import static org.springframework.util.StringUtils.hasText;

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
    private final BlockRepository blockRepository;
    private final FilterRepository filterRepository;

    // 팔로우하기
    public Long followUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);

        Follow follow = new Follow(user, targetUser);
        followRepository.save(follow);
        return follow.getId();
    }

    // 좋아요
    public Long like(Long userId, Long targetId, LikeType type) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        switch (type) {
            case PAGE:
                Page page = pageRepository.findById(targetId).orElseThrow(PageNotFoundException::new);
                return likePageRepository.save(new LikePage(user, page)).getId();

            case COMMENT:
                Comment comment = commentRepository.findById(targetId).orElseThrow(CommentNotFoundException::new);
                return likeCommentRepository.save(new LikeComment(user, comment)).getId();
        }

        throw new RuntimeException();
    }

    // 신고
    public Long report(Long userId, Long targetId, ReportType type) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        switch (type){
            case USER:
                User targetUser = userRepository.findById(targetId).orElseThrow(UserNotFoundException::new);
                return reportUserRepository.save(createReportUser(user, targetUser)).getId();

            case PAGE:
                Page targetPage = pageRepository.findById(targetId).orElseThrow(PageNotFoundException::new);
                return reportPageRepository.save(createReportPage(user, targetPage)).getId();

            case COMMENT:
                Comment targetComment = commentRepository.findById(targetId).orElseThrow(CommentNotFoundException::new);
                return reportCommentRepository.save(createReportComment(user, targetComment)).getId();

        }

        // 수정필요
        throw new RuntimeException();
    }

    // 차단
    public Long blockUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);

        Block block = createBlock(user, targetUser);
        return blockRepository.save(block).getId();
    }

    // 차단한 유저 전체조회
    @Transactional(readOnly = true)
    public List<BlockedUserDto> readBlockedUserList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return user.getBlockUserList()
                .stream()
                .map(block -> new BlockedUserDto(block.getTargetUser().getId(), block.getTargetUser().getUserProfile().getNickname()))
                .collect(Collectors.toList());
    }

    // 차단한 유저 차단해제
    public void unblockUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        for (Block block : user.getBlockUserList()) {
            if (block.getTargetUser().getId().equals(targetUserId)) {
                user.getBlockUserList().remove(block);
                blockRepository.delete(block);
            }
        }
    }

    // 필터링하기
    public Long filterUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);

        Filter filter = createFilter(user, targetUser);
        return filterRepository.save(filter).getId();
    }

    // 필터링한 유저 전체조회
    @Transactional(readOnly = true)
    public List<UserDto> readFilteredUserList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return user.getNotFilterUserList()
                .stream()
                .map(dto -> new UserDto(dto.getTargetUser().getId(), dto.getTargetUser().getUserProfile().getNickname(), dto.getTargetUser().getUserProfile().getImgUrl()))
                .collect(Collectors.toList());
    }

    // 필터링한 유저 필터링해제
    public void unfilterUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        for (Filter filter : user.getNotFilterUserList()) {
            if (filter.getTargetUser().getId().equals(targetUserId)) {
                user.getNotFilterUserList().remove(filter);
                filterRepository.delete(filter);
            }
        }
    }

    // 마이페이지
    // 프로필 편집
    public void changeUserProfile(Long userId, UserProfile userProfile) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changeUserProfile(userProfile);
    }

    // 푸쉬알림 설정
    public void changeUserNoticeSetting(Long userId, UserNoticeSetting userNoticeSetting) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changeUserNoticeSetting(userNoticeSetting);
    }

    // 비밀번호 변경
    public void changePassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(hasText(password)) user.changePassword(password);
        else throw new EmptyStringException();
    }
    
    // 필터링 설정
    public void changeUserFilterSetting(Long userId, UserFilterSetting userFilterSetting) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changeUserFilterSetting(userFilterSetting);
    }

    // 유저 검색
    @Transactional(readOnly = true)
    public List<UserDto> findUsersStartWithNickname(String nickname) {
        if (hasText(nickname)) return userRepository.findUsersStartWithNickname(nickname);
        else throw new EmptyStringException();
    }

}




