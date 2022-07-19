package cleanbook.com.service;

import cleanbook.com.domain.RefreshToken;
import cleanbook.com.domain.social.*;
import cleanbook.com.domain.user.*;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.repository.RefreshTokenRepository;
import cleanbook.com.repository.user.UserRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ProviderService {

    private final OAuthRequestFactory oAuthRequestFactory;
    private final UserService userService;
    private final UserRepository userRepository;
    private final Gson gson;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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
                        .id(kakaoProfile.getId())
                        .email(kakaoProfile.getKakao_account().getEmail())
                        .nickname(kakaoProfile.getKakao_account().getProfile().getNickname())
                        .gender(gender)
                        .build();

            case "naver":
                NaverProfile naverProfile = gson.fromJson(response, NaverProfile.class);
                gender = naverProfile.getResponse().getGender().equals("M") ? GenderType.MALE : GenderType.FEMALE;
                return SocialProfile.builder()
                        .id(naverProfile.getResponse().getId())
                        .email(naverProfile.getResponse().getEmail())
                        .nickname(naverProfile.getResponse().getNickname())
                        .gender(gender)
                        .build();

            case "google":
                return new SocialProfile();
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

            userService.signUp(signUpDto);
            user = userRepository.findUserByEmail(socialProfile.getEmail()).orElseThrow(UserNotFoundException::new);
        } else {
            user = findUser.get();
        }

        String accessToken = tokenProvider.createAccessToken(user.getId());
        String refreshToken = tokenProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));

        userService.addCookie(response, "X-AUTH-TOKEN", accessToken);
        userService.addCookie(response, "REFRESH-TOKEN", refreshToken);
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
                    .password(passwordEncoder.encode(socialProfile.getId()))
                    .nickname(socialProfile.getNickname())
                    .age(null)
                    .gender(socialProfile.getGender())
                    .build();

            userService.signUp(signUpDto);
            user = userRepository.findUserByEmail(socialProfile.getEmail()).orElseThrow(UserNotFoundException::new);
        } else {
            user = findUser.get();
        }

        String accessToken = tokenProvider.createAccessToken(user.getId());
        String refreshToken = tokenProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));

        userService.addCookie(response, "X-AUTH-TOKEN", accessToken);
        userService.addCookie(response, "REFRESH-TOKEN", refreshToken);
    }
}
