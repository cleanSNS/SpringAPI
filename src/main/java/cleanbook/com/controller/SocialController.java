package cleanbook.com.controller;

import cleanbook.com.domain.social.SocialAccessToken;
import cleanbook.com.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/social/login")
public class SocialController {

    private final Environment env;
    private final ProviderService providerService;

    @Value("${spring.social.kakao.client_id}")
    private String kakaoClientId;

    @Value("${spring.social.kakao.redirect}")
    private String kakaoRedirect;

    @Value("${spring.social.google.client_id}")
    private String googleClientId;

    @Value("${spring.social.google.redirect}")
    private String googleRedirect;

    @Value("${spring.social.naver.client_id}")
    private String naverClientId;

    @Value("${spring.social.naver.redirect}")
    private String naverRedirect;

    //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=75670ae520e9b0c56500f349b16c3c68&redirect_uri=http://localhost:8080/social/login/kakao
    @GetMapping("/kakao/code")
    public void code(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+kakaoClientId+"&redirect_uri="+kakaoRedirect);
    }

    @GetMapping("/kakao")
    public String kakaoSignUpAndLogin(@RequestParam String code, HttpServletResponse response) {
        providerService.socialSignUpAndLogin(code, "kakao", response);
        return code;
    }

    // postman 테스트위해
    @GetMapping("/kakao/postman")
    public String kakaoSignUpAndLoginTest(@RequestParam String token, HttpServletResponse response) {
        providerService.socialSignUpAndLoginPostman(token, "kakao", response);
        return token;
    }


    //https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=_A0bRpk1yPqnrmV8eBx8&state=state&redirect_uri=http://localhost:8080/social/login/naver
    @GetMapping("/naver")
    public String naverSignUpAndLogin(@RequestParam String code, HttpServletResponse response) {
        providerService.socialSignUpAndLogin(code, "naver", response);
        return code;
    }

    // postman 테스트위해
    @GetMapping("/naver/postman")
    public String naverSignUpAndLoginTest(@RequestParam String token, HttpServletResponse response) {
        providerService.socialSignUpAndLoginPostman(token, "naver", response);
        return token;
    }
}
