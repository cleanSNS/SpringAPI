package cleanbook.com.controller;

import cleanbook.com.dto.CountDto;
import cleanbook.com.dto.NotificationDto;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.UserPageDto;
import cleanbook.com.dto.user.*;
import cleanbook.com.exception.Response;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

import static cleanbook.com.entity.user.UserFilterSetting.createUserFilterSetting;
import static cleanbook.com.entity.user.UserNotificationSetting.createUserNotificationSetting;
import static cleanbook.com.entity.user.UserProfile.createUserProfile;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    // 팔로우하기
    @PostMapping("/user/follow")
    public ResponseEntity<Response> followUser(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody UserIdDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.followUser(userId, dto.getUserId());

        return ResponseEntity.ok(new Response("success"));
    }

    // 언팔로우하기
    @PostMapping("/user/unfollow")
    public ResponseEntity<Response> unfollowUser(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody UserIdDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.unfollowUser(userId, dto.getUserId());

        return ResponseEntity.ok(new Response("success"));
    }

    // 내가 팔로우하는 유저 전체 조회
    @GetMapping("/user/followee")
    public ResultDto<List<UserDto>> readFolloweeList(@CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        return userService.readFolloweeList(userId);
    }

    // 나를 팔로우하는 유저 전체 조회
    @GetMapping("/user/follower")
    public ResultDto<List<UserDto>> readFollowerList(@CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        return userService.readFollowerList(userId);
    }

    // 좋아요
    @PostMapping("/user/like")
    public ResponseEntity<Response> like(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody LikeDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.like(userId, dto.getTargetId(), dto.getType());

        return ResponseEntity.ok(new Response("success"));
    }

    // 좋아요 취소
    @PostMapping("/user/unlike")
    public ResponseEntity<Response> unlike(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody LikeDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.unlike(userId, dto.getTargetId(), dto.getType());

        return ResponseEntity.ok(new Response("success"));
    }

    // 좋아요 여부 확인(페이지, 댓글)
    @GetMapping("/user/like")
    public ResultDto<LikeCheckDto> isLike(@CookieValue("X-AUTH-TOKEN") String token, LikeDto dto) {
        Long userId = tokenProvider.getUserId(token);
        return userService.isLike(userId, dto.getTargetId(), dto.getType());
    }

    // 신고
    @PostMapping("/user/report")
    public ResponseEntity<Response> report(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody ReportDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.report(userId, dto.getTargetId(), dto.getType());

        return ResponseEntity.ok(new Response("success"));
    }

    // 차단
    @PostMapping("/user/block")
    public ResponseEntity<Response> block(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody UserIdDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.blockUser(userId, dto.getUserId());

        return ResponseEntity.ok(new Response("success"));
    }

    // 차단해제
    @PostMapping("/user/unblock")
    public ResponseEntity<Response> unblock(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody UserIdDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.unblockUser(userId, dto.getUserId());

        return ResponseEntity.ok(new Response("success"));
    }

    // 차단한 유저 전체조회
    @GetMapping("/user/block")
    public ResultDto<List<BlockedUserDto>> readBlockedUserList(@CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        return userService.readBlockedUserList(userId);
    }

    // 필터링하지 않을 사용자 추가
    @PostMapping("/user/unfilter")
    public ResponseEntity<Response> unfilter(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody UserIdDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.unfilterUser(userId, dto.getUserId());

        return ResponseEntity.ok(new Response("success"));
    }

    // 필터링하지 않을 사용자 해제
    @PostMapping("/user/filter")
    public ResponseEntity<Response> filter(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody UserIdDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.filterUser(userId, dto.getUserId());

        return ResponseEntity.ok(new Response("success"));
    }

    // 필터링하지 않을 사용자 조회
    @GetMapping("/user/unfilter")
    public ResultDto<List<UserDto>> readUnfilteredUserList(@CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        return userService.readUnfilteredUserList(userId);
    }

    // 마이페이지
    // 프로필 보기
    @GetMapping("/user/mypage/profile")
    public ResultDto<UserProfileDto> readUserProfile(@CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        return userService.readUserProfile(userId);
    }

    // 프로필 편집
    @PostMapping("/user/mypage/profile")
    public ResponseEntity<Response> changeUserProfile(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody UserProfileDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.changeUserProfile(userId, createUserProfile(dto));

        return ResponseEntity.ok(new Response("success"));
    }

    // 푸쉬알림 보기
    @GetMapping("/user/mypage/push")
    public ResultDto<UserNotificationSettingDto> readUserNotificationSetting(@CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        return userService.readUserNotificationSetting(userId);
    }

    // 푸쉬알림 설정
    @PostMapping("/user/mypage/push")
    public ResponseEntity<Response> changeUserNotificationSetting(@CookieValue("X-AUTH-TOKEN") String token,
                                                                  @RequestBody UserNotificationSettingDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.changeUserNotificationSetting(userId, createUserNotificationSetting(dto));

        return ResponseEntity.ok(new Response("success"));
    }

    // 기존 비밀번호 일치 여부확인
    @PostMapping("/user/mypage/password/check")
    public ResponseEntity<Boolean> checkPassword(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody UserPasswordDto dto) {
        Long userId = tokenProvider.getUserId(token);
        boolean result = userService.checkPassword(userId, dto.getPassword());

        return ResponseEntity.ok(result);
    }

    // 비밀번호 변경
    @PostMapping("/user/mypage/password/change")
    public ResponseEntity<Response> changePassword(@CookieValue("X-AUTH-TOKEN") String token, @Validated @RequestBody UserPasswordDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.changePassword(userId, dto.getPassword());

        return ResponseEntity.ok(new Response("success"));
    }

    // 비밀번호 초기화
    @PostMapping("/user/mypage/password/reset")
    public ResponseEntity<Response> resetPassword(@RequestBody UserEmailDto dto) throws MessagingException {
        userService.resetPassword(dto.getEmail());

        return ResponseEntity.ok(new Response("success"));
    }

    // 필터링 보기
    @GetMapping("/user/mypage/filter")
    public ResultDto<UserFilterSettingDto> readUserFilterSetting(@CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        return userService.readUserFilterSetting(userId);
    }

    // 필터링 설정
    @PostMapping("/user/mypage/filter")
    public ResponseEntity<Response> changeUserFilterSetting(@CookieValue("X-AUTH-TOKEN") String token,
                                                                  @RequestBody UserFilterSettingDto dto) {
        Long userId = tokenProvider.getUserId(token);
        userService.changeUserFilterSetting(userId, createUserFilterSetting(dto));

        return ResponseEntity.ok(new Response("success"));
    }

    // 유저 검색
    @GetMapping("/user/search")
    public ResultDto<List<UserDto>> findUsersStartWithNickname(@CookieValue(value = "X-AUTH-TOKEN", required = false) String token, @RequestParam String nickname) {
        Long userId = tokenProvider.getUserId(token);
        return userService.findUsersStartWithNickname(userId, nickname);
    }

    // 해시태그 검색
    @GetMapping("/user/search/hashtag")
    public ResultDto<List<UserPageDto>> findUsersStartWithNicknameAndHashtag(@CookieValue(value = "X-AUTH-TOKEN", required = false) String token,
                                                                             @RequestParam String hashtag, @RequestParam Long startId) {
        Long userId = tokenProvider.getUserId(token);
        return userService.findUsersStartWithNicknameAndPageByHashtag(userId, startId, hashtag);
    }

    // 유저 id 조회
    @GetMapping("/user/id")
    public ResultDto<UserIdDto> getUserId(@CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        return userService.getUserId(userId);
    }

    // 유저 프로필 조회
    @GetMapping("/user/{userId}/profile")
    public ResultDto<UserNicknameProfileDto> getUserProfile(@CookieValue(value = "X-AUTH-TOKEN", required = false) String token,
                                                            @PathVariable("userId") Long targetUserId) {
        Long userId = tokenProvider.getUserId(token);
        return userService.getUserProfile(userId, targetUserId);
    }

    // 알림 내역 전체조회
    @GetMapping("/user/notification")
    public ResultDto<List<NotificationDto>> readNotificationList(@CookieValue("X-AUTH-TOKEN") String token, @RequestParam Long startId) {
        Long userId = tokenProvider.getUserId(token);
        return userService.readNotificationList(userId, startId);
    }

    // 알림 읽음
    @PostMapping("/user/notification")
    public ResponseEntity<Response> checkNotification(@CookieValue("X-AUTH-TOKEN") String token, @RequestParam Long notificationId) {
        Long userId = tokenProvider.getUserId(token);
        userService.checkNotification(userId, notificationId);
        return ResponseEntity.ok(new Response("success"));
    }

    // 알림 삭제
    @DeleteMapping("/user/notification")
    public ResponseEntity<Response> deleteNotification(@CookieValue("X-AUTH-TOKEN") String token, @RequestParam Long notificationId) {
        Long userId = tokenProvider.getUserId(token);
        userService.deleteNotification(userId, notificationId);
        return ResponseEntity.ok(new Response("success"));
    }


    // 확인하지 않은 알림 갯수
    @GetMapping("/user/notification/count")
    public ResultDto<CountDto> notcheckedNotificationCount(@CookieValue("X-AUTH-TOKEN") String token){
        Long userId = tokenProvider.getUserId(token);
        return userService.notcheckedNotificationCount(userId);
    }


}
