package cleanbook.com.domain.social;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuthRequestFactory {

    private final KakaoInfo kakaoInfo;
    private final GoogleInfo googleInfo;
    private final NaverInfo naverInfo;

    public String getUri(String code, String provider) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        switch (provider){
            case "kakao":
                map.add("grant_type", "authorization_code");
                map.add("client_id", kakaoInfo.getClientId());
                map.add("redirect_uri", kakaoInfo.getRedirect());
                map.add("code", code);

                return UriComponentsBuilder
                        .fromUriString(kakaoInfo.tokenUrl)
                        .queryParams(map)
                        .toUriString();

            case "naver":
                map.add("grant_type", "authorization_code");
                map.add("client_id", naverInfo.getClientId());
                map.add("client_secret", naverInfo.getClientSecret());
                map.add("redirect_uri", naverInfo.getRedirect());
                map.add("state", "project");
                map.add("code", code);

                return UriComponentsBuilder
                        .fromUriString(naverInfo.tokenUrl)
                        .queryParams(map)
                        .toUriString();

            case "google":
                map.add("grant_type", "authorization_code");
                map.add("client_id", googleInfo.getClientId());
                map.add("client_secret", googleInfo.getClientSecret());
                map.add("redirect_uri", googleInfo.getRedirect());
                map.add("code", code);

                return UriComponentsBuilder
                        .fromUriString(googleInfo.tokenUrl)
                        .queryParams(map)
                        .toUriString();

        }
        throw new RuntimeException();
    }

    public String getProfileUrl(String provider) {
        if (provider.equals("kakao")) {
            return kakaoInfo.getProfileUrl();
        } else if(provider.equals("google")) {
            return googleInfo.getProfileUrl();
        } else {
            return naverInfo.getProfileUrl();
        }
    }

    @Getter
    @Component
    static class KakaoInfo {
        @Value("${spring.social.kakao.client_id}")
        String clientId;
        @Value("${spring.social.kakao.redirect}")
        String redirect;
        @Value("${spring.social.kakao.url.token}")
        private String tokenUrl;
        @Value("${spring.social.kakao.url.profile}")
        private String profileUrl;
    }

    @Getter
    @Component
    static class GoogleInfo {
        @Value("${spring.social.google.client_id}")
        String clientId;
        @Value("${spring.social.google.redirect}")
        String redirect;
        @Value("${spring.social.google.client_secret}")
        String clientSecret;
        @Value("${spring.social.google.url.token}")
        private String tokenUrl;
        @Value("${spring.social.google.url.profile}")
        private String profileUrl;
    }

    @Getter
    @Component
    static class NaverInfo {
        @Value("${spring.social.naver.client_id}")
        String clientId;
        @Value("${spring.social.naver.redirect}")
        String redirect;
        @Value("${spring.social.naver.client_secret}")
        String clientSecret;
        @Value("${spring.social.naver.url.token}")
        private String tokenUrl;
        @Value("${spring.social.naver.url.profile}")
        private String profileUrl;
    }
}
