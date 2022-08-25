package cleanbook.com.controller.local;

import cleanbook.com.exception.Response;
import cleanbook.com.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/local/social/login")
public class LocalSocialController {

    private final Environment env;
    private final ProviderService providerService;

    @Value("${spring.local.social.kakao.client_id}")
    private String kakaoClientId;

    @Value("${spring.local.social.kakao.redirect}")
    private String kakaoRedirect;

    @Value("${spring.local.social.naver.client_id}")
    private String naverClientId;

    @Value("${spring.local.social.naver.redirect}")
    private String naverRedirect;

    //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=75670ae520e9b0c56500f349b16c3c68&redirect_uri=http://localhost:8080/local/social/login/kakao
    @GetMapping("/kakao/code")
    public void kakaoCode(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+kakaoClientId+"&redirect_uri="+kakaoRedirect);
    }

    @GetMapping("/kakao")
    public ResponseEntity<Response> kakaoSignUpAndLogin(@RequestParam String code, HttpServletResponse response) {
        providerService.socialSignUpAndLogin(code, "kakao", response);
        return ResponseEntity.ok(new Response("success"));
    }

    // postman 테스트위해
    @GetMapping("/kakao/postman")
    public ResponseEntity<Response> kakaoSignUpAndLoginTest(@RequestParam String token, HttpServletResponse response) {
        providerService.socialSignUpAndLoginPostman(token, "kakao", response);
        return ResponseEntity.ok(new Response("success"));
    }

    //https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=_A0bRpk1yPqnrmV8eBx8&state=state&redirect_uri=http://localhost:8080/social/login/naver
    @GetMapping("/naver/code")
    public void naverCode(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="+naverClientId+"&state=state&redirect_uri="+naverRedirect);
    }

    @GetMapping("/naver")
    public ResponseEntity<Response> naverSignUpAndLogin(@RequestParam String code, HttpServletResponse response) {
        providerService.socialSignUpAndLogin(code, "naver", response);
        return ResponseEntity.ok(new Response("success"));
    }

    // postman 테스트위해
    @GetMapping("/naver/postman")
    public ResponseEntity<Response> naverSignUpAndLoginTest(@RequestParam String token, HttpServletResponse response) {
        providerService.socialSignUpAndLoginPostman(token, "naver", response);
        return ResponseEntity.ok(new Response("success"));
    }
}
