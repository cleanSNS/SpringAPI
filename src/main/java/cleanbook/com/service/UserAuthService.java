package cleanbook.com.service;

import cleanbook.com.dto.user.EmailAuthDto;
import cleanbook.com.dto.user.UserDeleteDto;
import cleanbook.com.dto.user.UserLoginDto;
import cleanbook.com.dto.user.UserSignUpDto;
import cleanbook.com.entity.enums.AccountState;
import cleanbook.com.entity.user.EmailAuth;
import cleanbook.com.entity.user.RefreshToken;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.UserProfile;
import cleanbook.com.entity.user.authority.Authority;
import cleanbook.com.exception.exceptions.*;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.repository.RefreshTokenRepository;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.repository.user.email.EmailAuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.UUID;

import static cleanbook.com.entity.user.authority.UserAuthority.createUserAuthority;

@Service
@AllArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailAuthRepository emailAuthRepository;
    private final EmailService emailService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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
                        .build())
                .build();

        createUserAuthority(user, authority);

        // 이메일 인증 객체 생성
        EmailAuth emailAuth = emailAuthRepository.save(
                EmailAuth.builder()
                        .email(userSignupDto.getEmail())
                        .authToken(UUID.randomUUID().toString())
                        .expired(false)
                        .build());

        emailService.send(userSignupDto.getEmail(), emailAuth.getAuthToken());

        return new UserSignUpDto(userRepository.save(user));
    }

    // 이메일 인증
    public void confirmEmail(EmailAuthDto requestDto) {
        EmailAuth emailAuth = emailAuthRepository.findValidAuthByEmail(requestDto.getEmail(), requestDto.getAuthToken(), LocalDateTime.now()).orElseThrow(EmailAuthTokenNotFoundException::new);
        User user = userRepository.findUserByEmail(requestDto.getEmail()).orElseThrow(UserNotFoundException::new);

        emailAuth.useToken();
        user.activateAccount();
    }

    // 로그인
    public UserLoginDto login(UserLoginDto userLoginDto, HttpServletResponse response) {
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
        addCookie(response, "REFRESH-TOKEN", refreshToken);

        return new UserLoginDto(user);
    }

    public void addCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie;
        cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // https-only
//        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    // refresh access token
    public void refreshToken(String accessToken, String refreshToken, HttpServletResponse response) {

        // accessToken이 만료되지 않았거나 토큰이 비어있을시
        if (!StringUtils.hasText(accessToken) || !StringUtils.hasText(refreshToken) || tokenProvider.validateToken(accessToken)) {
            throw new IllegalTokenException();
        }
        User user = userRepository.findById(tokenProvider.getUserId(refreshToken)).orElseThrow(UserNotFoundException::new);

        // refreshToken이 만료되지 않음
        if (tokenProvider.validateToken(refreshToken)) {
            String token = refreshTokenRepository.findByEmail(user.getEmail()).orElseThrow(TokenNotFoundException::new).getToken();

            // db에 저장된 refreshToken과 일치하지 않을시
            if (!token.equals(refreshToken)) {
                throw new IllegalTokenException();
            }

            // accessToken 재발급
            String newAccessToken = tokenProvider.createAccessToken(user.getId());
            addCookie(response, "X-AUTH-TOKEN", newAccessToken);
            return;
        }

        // refreshToken이 만료됨
        throw new TokenExpiredException();
    }

    // 로그아웃
    public void logout(HttpServletResponse response) {
        deleteCookie("X-AUTH-TOKEN", response);
        deleteCookie("REFRESH-TOKEN", response);
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
        userRepository.delete(user);
    }
}
