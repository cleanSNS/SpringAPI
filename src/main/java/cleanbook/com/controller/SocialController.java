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
    public String socialSignUpAndLogin(@RequestParam String code, HttpServletResponse response) {
        providerService.socialSignUpAndLogin(code, response);
        return code;
    }

    // postman 테스트위해
    @GetMapping("/kakao/postman")
    public String test(@RequestParam String token, HttpServletResponse response) {
        providerService.socialSignUpAndLoginPostman(token, response);
        return token;
    }

}
