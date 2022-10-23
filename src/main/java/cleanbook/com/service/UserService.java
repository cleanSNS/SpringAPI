package cleanbook.com.service;

import cleanbook.com.dto.CountDto;
import cleanbook.com.dto.NotificationDto;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.user.*;
import cleanbook.com.entity.notification.Notification;
import cleanbook.com.entity.notification.NotificationType;
import cleanbook.com.entity.page.Comment;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.user.*;
import cleanbook.com.entity.user.block.Block;
import cleanbook.com.entity.user.filter.Filter;
import cleanbook.com.entity.user.follow.Follow;
import cleanbook.com.entity.user.like.LikeComment;
import cleanbook.com.entity.user.like.LikePage;
import cleanbook.com.entity.user.like.LikeType;
import cleanbook.com.entity.enums.ReportType;
import cleanbook.com.exception.exceptions.*;
import cleanbook.com.repository.*;
import cleanbook.com.repository.comment.CommentRepository;
import cleanbook.com.repository.notification.NotificationRepository;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.*;
import cleanbook.com.repository.user.like.LikeCommentRepository;
import cleanbook.com.repository.user.like.LikePageRepository;
import cleanbook.com.repository.user.report.ReportCommentRepository;
import cleanbook.com.repository.user.report.ReportPageRepository;
import cleanbook.com.repository.user.report.ReportUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static cleanbook.com.entity.notification.Notification.createNotification;
import static cleanbook.com.entity.user.filter.Filter.createFilter;
import static cleanbook.com.entity.user.block.Block.createBlock;
import static cleanbook.com.entity.user.follow.Follow.createFollow;
import static cleanbook.com.entity.user.like.LikeComment.createLikeComment;
import static cleanbook.com.entity.user.like.LikePage.createLikePage;
import static cleanbook.com.entity.user.report.ReportComment.createReportComment;
import static cleanbook.com.entity.user.report.ReportPage.createReportPage;
import static cleanbook.com.entity.user.report.ReportUser.createReportUser;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
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
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final AwsS3Service awsS3Service;

    @Value("${s3.default.profile.img}")
    private final String DEFAULT_PROFILE_IMG_URL;

    // 팔로우하기
    public Follow followUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);

        // 이미 팔로우 했으면 에러 발생
        if (followRepository.findByUser_IdAndTargetUser_Id(userId, targetUserId).isPresent()) {
            throw new MyException("이미 팔로우했습니다.");
        }

        Follow follow = createFollow(user, targetUser);
        followRepository.save(follow);

        // 알림 저장
        notificationRepository.save(createNotification(user, targetUser,NotificationType.FOLLOW, null));
        // SSE 송신
        Long count = notificationRepository.uncheckedNotificationCount(targetUser.getId()).getData().getCount();
        notificationService.sendNotificationCount(targetUser.getId(), count);

        return follow;
    }

    // 언팔로우하기
    public void unfollowUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);
        Follow follow = followRepository.findByUser_IdAndTargetUser_Id(userId, targetUserId).orElseThrow(() -> new NotFoundException("팔로우"));

        user.unfollow(follow);
        targetUser.unfollowed(follow);
        followRepository.delete(follow);
    }

    // 내가 팔로우하는 유저 전체 조회
    public ResultDto<List<UserDto>> readFolloweeList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<UserDto> userDtoList = user.getFolloweeList().stream()
                .map(f -> new UserDto(f.getTargetUser().getId(), f.getTargetUser().getUserProfile().getNickname(), f.getTargetUser().getUserProfile().getImgUrl()))
                .collect(Collectors.toList());

        return new ResultDto<>(userDtoList);
    }

    // 나를 팔로우하는 유저 전체 조회
    public ResultDto<List<UserDto>> readFollowerList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<UserDto> userDtoList = user.getFollowerList().stream()
                .map(f -> new UserDto(f.getUser().getId(), f.getUser().getUserProfile().getNickname(), f.getUser().getUserProfile().getImgUrl()))
                .collect(Collectors.toList());

        return new ResultDto<>(userDtoList);
    }

    // 좋아요
    public void like(Long userId, Long targetId, LikeType type) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        switch (type) {
            case PAGE:
                Page page = pageRepository.findById(targetId).orElseThrow(PageNotFoundException::new);
                User targetUser = userRepository.findById(page.getUser().getId()).orElseThrow(UserNotFoundException::new);
                // 자추 금지
                if (page.getUser().getId().equals(user.getId())) {
                    throw new MyException("자신의 게시글에는 좋아요 할 수 없습니다.");
                }
                // 이미 좋아요 한 게시글이면
                if (likePageRepository.findByPage_IdAndUser_Id(page.getId(), user.getId()).isPresent()) {
                    throw new MyException("이미 좋아요 한 게시글입니다.");
                }
                likePageRepository.save(createLikePage(user, page));

                // 좋아요 알림 허용했다면 알림 생성
                // todo SSE 알림+1
                if (page.getPageSetting().getNotificationLike()) {
                    notificationRepository.save(createNotification(user,targetUser,NotificationType.LIKE, page.getId()));
                }

                return;

            case COMMENT:
                Comment comment = commentRepository.findById(targetId).orElseThrow(CommentNotFoundException::new);

                // 자추 금지
                if (comment.getUser().getId().equals(user.getId())) {
                    throw new MyException("자신의 댓글에는 좋아요 할 수 없습니다.");
                }
                // 이미 좋아요 한 댓글이면
                if (likeCommentRepository.findByComment_IdAndUser_Id(comment.getId(), user.getId()).isPresent()) {
                    throw new MyException("이미 좋아요 한 댓글입니다.");
                }
                likeCommentRepository.save(createLikeComment(user, comment));

                return;
        }

        throw new MyException("잘못된 양식입니다.");
    }

    // 좋아요 취소
    public void unlike(Long userId, Long targetId, LikeType type) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        switch (type) {
            case PAGE:
                Page page = pageRepository.findById(targetId).orElseThrow(PageNotFoundException::new);
                LikePage likePage = likePageRepository.findByPage_IdAndUser_Id(page.getId(), user.getId()).orElseThrow(() -> new MyException("이미 좋아요 취소가 된 게시글입니다."));
                page.unlikePage();
                likePageRepository.delete(likePage);
                return;

            case COMMENT:
                Comment comment = commentRepository.findById(targetId).orElseThrow(CommentNotFoundException::new);
                LikeComment likeComment = likeCommentRepository.findByComment_IdAndUser_Id(comment.getId(), user.getId()).orElseThrow(() -> new MyException("이미 좋아요 취소가 된 댓글입니다."));
                comment.unlikeComment();
                likeCommentRepository.delete(likeComment);
                return;
        }

        throw new MyException("잘못된 양식입니다.");
    }


    // 좋아요 여부 확인
    public ResultDto<LikeCheckDto> isLike(Long userId, Long targetId, LikeType type) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        switch (type) {
            case PAGE:
                Page page = pageRepository.findById(targetId).orElseThrow(PageNotFoundException::new);
                return new ResultDto<>(new LikeCheckDto(likePageRepository.findByPage_IdAndUser_Id(page.getId(), user.getId()).isPresent()));

            case COMMENT:
                Comment comment = commentRepository.findById(targetId).orElseThrow(CommentNotFoundException::new);
                return new ResultDto<>(new LikeCheckDto(likeCommentRepository.findByComment_IdAndUser_Id(comment.getId(), user.getId()).isPresent()));

        }
        throw new MyException("잘못된 양식입니다.");
    }

    // 신고
    public Long report(Long userId, Long targetId, ReportType type) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        switch (type){
            case USER:
                User targetUser = userRepository.findById(targetId).orElseThrow(UserNotFoundException::new);

                // 중복 신고 방지
                if (reportUserRepository.findByUser_IdAndTargetUser_Id(userId, targetId).isPresent()) {
                    throw new MyException("이미 신고한 유저입니다.");
                }

                return reportUserRepository.save(createReportUser(user, targetUser)).getId();

            case PAGE:
                Page targetPage = pageRepository.findById(targetId).orElseThrow(PageNotFoundException::new);

                // 중복 신고 방지
                if (reportPageRepository.findByUser_IdAndTargetPage_Id(userId, targetId).isPresent()) {
                    throw new MyException("이미 신고한 게시글입니다.");
                }

                return reportPageRepository.save(createReportPage(user, targetPage)).getId();

            case COMMENT:
                Comment targetComment = commentRepository.findById(targetId).orElseThrow(CommentNotFoundException::new);

                // 중복 신고 방지
                if (reportCommentRepository.findByUser_IdAndTargetComment_Id(userId, targetId).isPresent()) {
                    throw new MyException("이미 신고한 댓글입니다.");
                }

                return reportCommentRepository.save(createReportComment(user, targetComment)).getId();

        }

        // 수정필요
        throw new RuntimeException();
    }

    // 차단
    public void blockUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);

        // 중복 차단 방지
        if (blockRepository.findByUser_IdAndTargetUser_Id(userId, targetUserId).isPresent()) {
            throw new MyException("이미 차단한 유저입니다.");
        }

        // 팔로우중이라면 언팔로우
        if (followRepository.findByUser_IdAndTargetUser_Id(userId, targetUserId).isPresent()) {
            unfollowUser(userId, targetUserId);
        }

        // 팔로우중이라면 언팔로우 당함
        if (followRepository.findByUser_IdAndTargetUser_Id(targetUserId, userId).isPresent()) {
            unfollowUser(targetUserId, userId);
        }

        blockRepository.save(createBlock(user, targetUser));
    }

    // 차단해제
    public void unblockUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Block block = blockRepository.findByUser_IdAndTargetUser_Id(userId, targetUserId).orElseThrow(() -> new MyException("이미 차단 해제된 유저입니다."));

        user.getBlockedUserList().remove(block);
        blockRepository.delete(block);
    }

    // 차단한 유저 전체조회
    @Transactional(readOnly = true)
    public ResultDto<List<BlockedUserDto>> readBlockedUserList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<BlockedUserDto> blockedUserDtoList = user.getBlockedUserList()
                .stream()
                .map(block -> new BlockedUserDto(block.getTargetUser().getId(), block.getTargetUser().getUserProfile().getNickname(), block.getTargetUser().getUserProfile().getImgUrl()))
                .collect(Collectors.toList());

        return new ResultDto<>(blockedUserDtoList);
    }

    // 필터링하지 않을 사용자 추가
    public void unfilterUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);

        // todo 필터링 메서드
        Filter filter = createFilter(user, targetUser);
        filterRepository.save(filter);
    }

    // 필터링하지 않을 사용자 해제
    public void filterUser(Long userId, Long targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Filter filter = filterRepository.findByUser_IdAndTargetUser_Id(userId, targetUserId).orElseThrow(() -> new MyException("이미 해제된 유저입니다."));

        user.getNotFilterUserList().remove(filter);
        filterRepository.delete(filter);
    }

    // 필터링한 유저 전체조회
    @Transactional(readOnly = true)
    public ResultDto<List<UserDto>> readUnfilteredUserList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<UserDto> userDtoList = user.getNotFilterUserList()
                .stream()
                .map(dto -> new UserDto(dto.getTargetUser().getId(), dto.getTargetUser().getUserProfile().getNickname(), dto.getTargetUser().getUserProfile().getImgUrl()))
                .collect(Collectors.toList());

        return new ResultDto<>(userDtoList);
    }


    // 마이페이지
    // 프로필 보기
    public ResultDto<UserProfileDto> readUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return new ResultDto<>(new UserProfileDto(user.getUserProfile()));
    }

    // 프로필 편집
    public void changeUserProfile(Long userId, UserProfile userProfile) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        // 프로필 이미지가 변경 되었을시 기존 이미지 s3에서 삭제
        if (!user.getUserProfile().getImgUrl().equals(DEFAULT_PROFILE_IMG_URL) && !user.getUserProfile().getImgUrl().equals(userProfile.getImgUrl())) {
            awsS3Service.deleteFiles(Collections.singletonList(user.getUserProfile().getImgUrl()));
        }
        user.changeUserProfile(userProfile);
    }

    // 푸쉬알림 보기
    public ResultDto<UserNotificationSettingDto> readUserNotificationSetting(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return new ResultDto<>(new UserNotificationSettingDto(user.getUserSetting().getUserNotificationSetting()));
    }

    // 푸쉬알림 설정
    public void changeUserNotificationSetting(Long userId, UserNotificationSetting userNotificationSetting) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changeUserNotificationSetting(userNotificationSetting);
    }

    // 이전비밀번호 일치 여부 확인
    public boolean checkPassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return passwordEncoder.matches(password, user.getPassword());
    }

    // 비밀번호 변경
    public void changePassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changePassword(passwordEncoder.encode(password));
        log.info("changedPassword = " + password);
    }

    // 비밀번호 초기화
    public void resetPassword(String email) throws MessagingException {
        // 임의로 비밀번호 변경
        String newPassword = generateNewPassword();
        User user = userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
        user.changePassword(passwordEncoder.encode(newPassword));

        // 변경된 비밀번호 유저 이메일로 전송
        emailService.sendNewPassword(email, newPassword);
        log.info("changedPassword = " + newPassword);
    }

    // 임의의 숫자,영어소문자, 대문자, 특수기호, 10자리 문자열
    public String generateNewPassword(){
        int leftLimit = 48; // letter '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // 필터링 보기
    public ResultDto<UserFilterSettingDto> readUserFilterSetting(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return new ResultDto<>(new UserFilterSettingDto(user.getUserSetting().getUserFilterSetting()));
    }
    
    // 필터링 설정
    public void changeUserFilterSetting(Long userId, UserFilterSetting userFilterSetting) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changeUserFilterSetting(userFilterSetting);
    }

    // 유저 검색
    @Transactional(readOnly = true)
    public ResultDto<List<UserDto>> findUsersStartWithNickname(Long userId, String nickname) {

        if (hasText(nickname)) {
            return userRepository.findUsersStartWithNickname(userId, nickname);
        }
        else {
            throw new EmptyStringException();
        }
    }


    // 유저 ID 조회
    public ResultDto<UserIdDto> getUserId(Long userId) {
        return new ResultDto<>(new UserIdDto(userId));
    }

    // 유저 프로필 조회
    public ResultDto<UserNicknameProfileDto> getUserProfile(Long userId, Long targetUserId) {
        User user = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);
        UserNicknameProfileDto userNicknameProfileDto = UserNicknameProfileDto.builder()
                .nickname(user.getUserProfile().getNickname())
                .imgUrl(user.getUserProfile().getImgUrl())
                .selfIntroduction(user.getUserProfile().getSelfIntroduction())
                .followerCount(user.getFollowerCount())
                .followeeCount(user.getFolloweeCount())
                .follow(followRepository.findByUser_IdAndTargetUser_Id(userId, targetUserId).isPresent())
                .build();

        return new ResultDto<>(userNicknameProfileDto);
    }

    // 알림 내역 전체 조회
    public ResultDto<List<NotificationDto>> readNotificationList(Long userId, Long startId) {
        return notificationRepository.readNotificationList(userId, startId, 10);
    }

    // 알림 읽음
    public void checkNotification(Long userId, Long notificationId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NotFoundException("알림"));
        if (!notification.getTargetUser().getId().equals(user.getId())) {
            throw new MyException("잘못된 접근입니다.");
        }
        notification.checkNotification();
    }

    // 알림 삭제
    public void deleteNotification(Long userId, Long notificationId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NotFoundException("알림"));
        if (!notification.getTargetUser().getId().equals(user.getId())) {
            throw new MyException("잘못된 접근입니다.");
        }
        notificationRepository.delete(notification);
    }

    // 확인하지 않은 알림 갯수
    public ResultDto<CountDto> notcheckedNotificationCount(Long userId) {
        return notificationRepository.uncheckedNotificationCount(userId);
    }



}




