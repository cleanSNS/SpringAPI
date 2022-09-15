package cleanbook.com.service;

import cleanbook.com.dto.user.UserSignUpDto;
import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.user.RefreshToken;
import cleanbook.com.entity.social.*;
import cleanbook.com.entity.user.*;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.repository.RefreshTokenRepository;
import cleanbook.com.repository.UserActiveRepository;
import cleanbook.com.repository.user.UserRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class ProviderService {

    private final OAuthRequestFactory oAuthRequestFactory;
    private final UserAuthService userAuthService;
    private final UserRepository userRepository;
    private final Gson gson;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserActiveRepository userActiveRepository;

    // access 토큰 받아오기
    public SocialAccessToken getAccessToken(String code, String provider) {

        String uri = oAuthRequestFactory.getUri(code, provider);

        ResponseEntity<String> response = WebClient.create()
                .post()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .retrieve()
                .toEntity(String.class)
                .block();

        log.info("response {}", response);
        log.info("response {}", response.getBody());

        try {
            if (response.getStatusCode() == HttpStatus.OK) {
                return gson.fromJson(response.getBody(), SocialAccessToken.class);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    // access 토큰 바탕으로 유저 프로필 받아오기
    public SocialProfile getProfile(String accessToken, String provider) {

        ResponseEntity<String> response = WebClient.create()
                .post()
                .uri(oAuthRequestFactory.getProfileUrl(provider))
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class)
                .block();

        log.info("response {}", response);
        log.info("response {}", response.getBody());

        try {
            if (response.getStatusCode() == HttpStatus.OK) {
                return parseJsonToClass(response.getBody(), provider);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    private SocialProfile parseJsonToClass(String response, String provider) {
        GenderType gender;
        switch (provider) {
            case "kakao":
                KakaoProfile kakaoProfile = gson.fromJson(response, KakaoProfile.class);
                gender = kakaoProfile.getKakao_account().getGender().equals("male") ? GenderType.MALE : GenderType.FEMALE;
                return SocialProfile.builder()
                        .email(kakaoProfile.getKakao_account().getEmail())
                        .nickname(kakaoProfile.getKakao_account().getProfile().getNickname())
                        .gender(gender)
                        .build();

            case "naver":
                NaverProfile naverProfile = gson.fromJson(response, NaverProfile.class);
                gender = naverProfile.getResponse().getGender().equals("M") ? GenderType.MALE : GenderType.FEMALE;
                return SocialProfile.builder()
                        .email(naverProfile.getResponse().getEmail())
                        .nickname(naverProfile.getResponse().getNickname())
                        .gender(gender)
                        .build();

        }
        throw new RuntimeException();
    }

    // 소셜 회원가입 및 로그인
    public void socialSignUpAndLogin(String code, String provider, HttpServletResponse response) {
        SocialAccessToken socialAccessToken = getAccessToken(code, provider);
        SocialProfile socialProfile = getProfile(socialAccessToken.getAccess_token(), provider);

        Optional<User> findUser = userRepository.findUserByEmail(socialProfile.getEmail());
        User user;
        // 신규 가입일시
        if (findUser.isEmpty()) {
            UserSignUpDto signUpDto = UserSignUpDto.builder()
                    .email(socialProfile.getEmail())
                    .password(passwordEncoder.encode(socialProfile.getEmail()))
                    .nickname(socialProfile.getNickname())
                    .age(null)
                    .gender(socialProfile.getGender())
                    .build();

            // 소셜 회원가입은 이메일 인증이 필요없음
            userActiveRepository.save(new UserActive(socialProfile.getEmail(), true));
            userAuthService.signUp(signUpDto);
            user = userRepository.findUserByEmail(socialProfile.getEmail()).orElseThrow(UserNotFoundException::new);
        } else {
            user = findUser.get();
        }

        String accessToken = tokenProvider.createAccessToken(user.getId());
        String refreshToken = tokenProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));

        userAuthService.addCookie(response, "X-AUTH-TOKEN", accessToken);
        response.setHeader("Authorization", "Bearer " + refreshToken);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        user.activateAccount();
    }

    // postman 테스트위해
    public void socialSignUpAndLoginPostman(String token, String provider, HttpServletResponse response) {
        SocialProfile socialProfile = getProfile(token, provider);

        Optional<User> findUser = userRepository.findUserByEmail(socialProfile.getEmail());
        User user;
        // 신규 가입일시
        if (findUser.isEmpty()) {
            UserSignUpDto signUpDto = UserSignUpDto.builder()
                    .email(socialProfile.getEmail())
                    .password(passwordEncoder.encode(socialProfile.getEmail()))
                    .nickname(socialProfile.getNickname())
                    .age(null)
                    .gender(socialProfile.getGender())
                    .build();

            userActiveRepository.save(new UserActive(socialProfile.getEmail(), true));
            userAuthService.signUp(signUpDto);
            user = userRepository.findUserByEmail(socialProfile.getEmail()).orElseThrow(UserNotFoundException::new);
        } else {
            user = findUser.get();
        }

        String accessToken = tokenProvider.createAccessToken(user.getId());
        String refreshToken = tokenProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));

        userAuthService.addCookie(response, "X-AUTH-TOKEN", accessToken);
        response.setHeader("Authorization", "Bearer " + refreshToken);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        user.activateAccount();
    }
}
