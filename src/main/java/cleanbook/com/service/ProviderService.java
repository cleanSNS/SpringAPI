package cleanbook.com.service;

import cleanbook.com.domain.RefreshToken;
import cleanbook.com.domain.social.*;
import cleanbook.com.domain.user.*;
import cleanbook.com.exception.exceptions.DuplicateUserException;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.repository.RefreshTokenRepository;
import cleanbook.com.repository.user.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.naming.CommunicationException;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

import static cleanbook.com.domain.user.UserAuthority.createUserAuthority;

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

        String uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/token")
                .queryParams(oAuthRequestFactory.getMultiMap(code, provider))
                .toUriString();

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

    public KakaoProfile getProfile(String accessToken, String provider) {

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
                return gson.fromJson(response.getBody(), KakaoProfile.class);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        throw new RuntimeException();
    }

    public void socialSignUpAndLogin(String code, HttpServletResponse response) {
        SocialAccessToken socialAccessToken = getAccessToken(code, "kakao");
        KakaoProfile kakaoProfile = getProfile(socialAccessToken.getAccess_token(), "kakao");

        Optional<User> findUser = userRepository.findUserByEmail(kakaoProfile.getKakao_account().getEmail());
        User user;
        // 신규 가입일시
        if (findUser.isEmpty()) {
            GenderType gender = kakaoProfile.getKakao_account().getGender().equals("male") ? GenderType.MALE : GenderType.FEMALE;
            UserSignUpDto signUpDto = UserSignUpDto.builder()
                    .email(kakaoProfile.getKakao_account().getEmail())
                    .password(passwordEncoder.encode(kakaoProfile.getId().toString()))
                    .nickname(kakaoProfile.getKakao_account().getProfile().getNickname())
                    .age(null)
                    .gender(gender)
                    .build();

            log.info("password {}", kakaoProfile.getId().toString());
            log.info("encodedPassword {}", passwordEncoder.encode(kakaoProfile.getId().toString()));
            userService.signUp(signUpDto);
            user = userRepository.findUserByEmail(kakaoProfile.getKakao_account().getEmail()).orElseThrow(UserNotFoundException::new);
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
    public void socialSignUpAndLoginPostman(String token, HttpServletResponse response) {
        KakaoProfile kakaoProfile = getProfile(token, "kakao");

        Optional<User> findUser = userRepository.findUserByEmail(kakaoProfile.getKakao_account().getEmail());
        User user;
        // 신규 가입일시
        if (findUser.isEmpty()) {
            GenderType gender = kakaoProfile.getKakao_account().getGender().equals("male") ? GenderType.MALE : GenderType.FEMALE;
            UserSignUpDto signUpDto = UserSignUpDto.builder()
                    .email(kakaoProfile.getKakao_account().getEmail())
                    .password(kakaoProfile.getId().toString())
                    .nickname(kakaoProfile.getKakao_account().getProfile().getNickname())
                    .age(null)
                    .gender(gender)
                    .build();

            userService.signUp(signUpDto);
            user = userRepository.findUserByEmail(kakaoProfile.getKakao_account().getEmail()).orElseThrow(UserNotFoundException::new);
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
