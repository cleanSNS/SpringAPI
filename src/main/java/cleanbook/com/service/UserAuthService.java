package cleanbook.com.service;

import cleanbook.com.dto.user.*;
import cleanbook.com.entity.enums.AccountState;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.page.PageImgUrl;
import cleanbook.com.entity.user.*;
import cleanbook.com.entity.user.authority.Authority;
import cleanbook.com.exception.exceptions.*;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.repository.RefreshTokenRepository;
import cleanbook.com.repository.UserActiveRepository;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.repository.user.email.EmailAuthRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

import static cleanbook.com.entity.user.authority.UserAuthority.createUserAuthority;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailAuthRepository emailAuthRepository;
    private final EmailService emailService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserActiveRepository userActiveRepository;
    private final AwsS3Service awsS3Service;

    @Value("${s3.default.profile.imgurl}")
    private String DEFAULT_PROFILE_IMG_URL;

    // 회원가입
    public UserSignUpDto signUp(UserSignUpDto userSignupDto) {

        // 유저 중복 검사
        if (userRepository.findUserByEmail(userSignupDto.getEmail()).isPresent()) {
            throw new UserDuplicateException();
        }

        // 유저 생성
        Authority authority = Authority.builder()
                .name("ROLE_USER")
                .build();

        User user = User.builder()
                .email(userSignupDto.getEmail())
                .password(passwordEncoder.encode(userSignupDto.getPassword()))
                .accountState(AccountState.INACTIVE)
                .userProfile(UserProfile.builder()
                        .nickname(userSignupDto.getNickname())
                        .age(userSignupDto.getAge())
                        .gender(userSignupDto.getGender())
                        .ageVisible(userSignupDto.isAgeVisible())
                        .genderVisible(userSignupDto.isGenderVisible())
                        .imgUrl(DEFAULT_PROFILE_IMG_URL)
                        .build())
                .userSetting(UserSetting.builder()
                        .userNotificationSetting(UserNotificationSetting.builder().build())
                        .userFilterSetting(UserFilterSetting.builder().build())
                        .build())
                .build();

        createUserAuthority(user, authority);

        if (userActiveRepository.findByEmail(userSignupDto.getEmail()).orElseThrow(EmailAuthFailException::new).isActive()) {
            user.activateAccount();
        }

        return new UserSignUpDto(userRepository.save(user));
    }

    // 이메일 인증 요청 보내기
    public void requestEmailServer(String email) throws MessagingException {
        // 이메일 인증 객체 생성
        EmailAuth emailAuth = emailAuthRepository.save(
                EmailAuth.builder()
                        .email(email)
                        .authToken(UUID.randomUUID().toString())
                        .expired(false)
                        .build());

        emailService.sendServer(email, emailAuth.getAuthToken());
    }

    public void requestEmailLocal(String email) throws MessagingException {
        // 이메일 인증 객체 생성
        EmailAuth emailAuth = emailAuthRepository.save(
                EmailAuth.builder()
                        .email(email)
                        .authToken(UUID.randomUUID().toString())
                        .expired(false)
                        .build());

        emailService.sendLocal(email, emailAuth.getAuthToken());
    }

    // 이메일 인증
    public void confirmEmail(EmailAuthDto requestDto) {
        EmailAuth emailAuth = emailAuthRepository.findValidAuthByEmail(requestDto.getEmail(), requestDto.getAuthToken(), LocalDateTime.now()).orElseThrow(EmailAuthTokenNotFoundException::new);
        userActiveRepository.save(new UserActive(requestDto.getEmail(), true));

        emailAuth.useToken();
    }

    // 로그인
    public UserIdDto login(UserLoginDto userLoginDto, HttpServletResponse response) {
        User user = userRepository.findUserByEmail(userLoginDto.getEmail())
                .orElseThrow(IllegalAccountException::new);

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new IllegalAccountException();
        }

        // 이메일 인증 검증
        if (!user.getAccountState().equals(AccountState.ACTIVE)) {
            throw new EmailAuthFailException();
        }

        String accessToken = tokenProvider.createAccessToken(user.getId());
        String refreshToken = tokenProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));

        addCookie(response, "X-AUTH-TOKEN", accessToken);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        response.setHeader("Authorization", "Bearer " + refreshToken);

        return new UserIdDto(user.getId());
    }

    // 로컬 로그인
    public UserIdDto loginLocal(UserLoginDto userLoginDto, HttpServletResponse response) {
        User user = userRepository.findUserByEmail(userLoginDto.getEmail())
                .orElseThrow(IllegalAccountException::new);

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new IllegalAccountException();
        }

        // 이메일 인증 검증
        if (!user.getAccountState().equals(AccountState.ACTIVE)) {
            throw new EmailAuthFailException();
        }

        String accessToken = tokenProvider.createAccessToken(user.getId());
        String refreshToken = tokenProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));

        addCookieLocal(response, "X-AUTH-TOKEN", accessToken);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        response.setHeader("Authorization", "Bearer " + refreshToken);

        return new UserIdDto(user.getId());
    }

    public void addCookie(HttpServletResponse response, String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .maxAge(1800)
                .secure(true)
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void addCookieLocal(HttpServletResponse response, String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .maxAge(1800)
                .path("/")
                .httpOnly(true)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    // refresh access token
    public void refreshToken(String accessToken, String refreshToken, HttpServletResponse response) {
        // bearer 제거
        refreshToken = refreshToken.substring("bearer".length()+1);

        // accessToken이 만료되지 않았거나 토큰이 비어있을시
        if (tokenProvider.validateToken(accessToken)) {
            throw new NotExpiredTokenException();
        }

        // refreshToken이 만료되지 않음
        if (tokenProvider.validateToken(refreshToken)) {
            User user = userRepository.findById(tokenProvider.getUserId(refreshToken)).orElseThrow(UserNotFoundException::new);
            String token = refreshTokenRepository.findByEmail(user.getEmail()).orElseThrow(TokenNotFoundException::new).getToken();

            // db에 저장된 refreshToken과 일치하지 않을시
            if (!token.equals(refreshToken)) {
                throw new MyException("올바르지 않은 refresh token입니다.");
            }

            // accessToken 재발급
            String newAccessToken = tokenProvider.createAccessToken(user.getId());
            addCookie(response, "X-AUTH-TOKEN", newAccessToken);

        } else { // refreshToken이 만료됨
            throw new MyException("refresh token이 만료되었습니다.");
        }
    }

    // refresh access token
    public void refreshTokenLocal(String accessToken, String refreshToken, HttpServletResponse response) {
        // bearer 제거
        refreshToken = refreshToken.substring("bearer".length()+1);

        // accessToken이 만료되지 않았거나 토큰이 비어있을시
        if (tokenProvider.validateToken(accessToken)) {
            throw new NotExpiredTokenException();
        }

        // refreshToken이 만료되지 않음
        if (tokenProvider.validateToken(refreshToken)) {
            User user = userRepository.findById(tokenProvider.getUserId(refreshToken)).orElseThrow(UserNotFoundException::new);
            String token = refreshTokenRepository.findByEmail(user.getEmail()).orElseThrow(TokenNotFoundException::new).getToken();

            // db에 저장된 refreshToken과 일치하지 않을시
            if (!token.equals(refreshToken)) {
                throw new MyException("올바르지 않은 refresh token입니다.");
            }

            // accessToken 재발급
            String newAccessToken = tokenProvider.createAccessToken(user.getId());
            addCookieLocal(response, "X-AUTH-TOKEN", newAccessToken);

        } else { // refreshToken이 만료됨
            throw new MyException("refresh token이 만료되었습니다.");
        }
    }

    // 로그아웃
    public void logout(HttpServletResponse response) {
        deleteCookie("X-AUTH-TOKEN", response);
    }

    public void deleteCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // 회원탈퇴
    public void delete(UserDeleteDto userDeleteDto, HttpServletResponse response) {
        User user = userRepository.findById(userDeleteDto.getUserId()).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(userDeleteDto.getPassword(), user.getPassword())) {
            throw new IllegalAccountException();
        }

        logout(response);
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(user.getEmail()).orElseThrow(TokenNotFoundException::new);
        refreshTokenRepository.delete(refreshToken);
        // s3 프로필 이미지 삭제
        awsS3Service.deleteFiles(Collections.singletonList(user.getUserProfile().getImgUrl()));
        // page 이미지 삭제
        for (Page page : user.getPageList()) {
            awsS3Service.deleteFiles(page.getImgUrlList().stream().map(PageImgUrl::getImgUrl).collect(Collectors.toList()));
        }
        userRepository.delete(user);
    }
}
