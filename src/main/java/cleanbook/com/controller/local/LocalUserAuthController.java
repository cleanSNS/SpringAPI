package cleanbook.com.controller.local;

import cleanbook.com.dto.user.*;
import cleanbook.com.exception.Response;
import cleanbook.com.service.EmailService;
import cleanbook.com.service.UserAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/local/user/auth")
@AllArgsConstructor
public class LocalUserAuthController {

    private final UserAuthService userAuthService;
    private final EmailService emailService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserSignUpDto> signUp(@Validated @RequestBody UserSignUpDto userSignUpDto) {
        return ResponseEntity.ok(userAuthService.signUp(userSignUpDto));
    }

    // 이메일 인증
    @GetMapping("/signup/confirm")
    public ResponseEntity<Response> authenticateEmail(@ModelAttribute EmailAuthDto emailAuthDto) {
        userAuthService.confirmEmail(emailAuthDto);
        return ResponseEntity.ok(new Response("success"));
    }

    // 이메일 인증 코드요청
    @GetMapping("/signup/request")
    public ResponseEntity<Response> requestEmailLink(@RequestBody EmailDto emailDto) throws MessagingException {
        userAuthService.requestEmailLocal(emailDto.getEmail());
        return ResponseEntity.ok(new Response("success"));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserLoginDto> login(@Validated @RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
        return ResponseEntity.ok(userAuthService.login(userLoginDto, response));
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletResponse response){
        userAuthService.logout(response);
        return ResponseEntity.ok(new Response("success"));
    }

    // 회원탈퇴
    @PostMapping("/delete")
    public ResponseEntity<Response> delete(@Valid @RequestBody UserDeleteDto userDeleteDto, HttpServletResponse response) {
        userAuthService.delete(userDeleteDto, response);
        return ResponseEntity.ok(new Response("success"));
    }

    // refresh accessToken
    @GetMapping("/refresh")
    public ResponseEntity<Response> refresh(@CookieValue(name = "X-AUTH-TOKEN") String accessToken,
                                            @RequestHeader("Authorization") String refreshToken,
                                        HttpServletResponse response) {

        userAuthService.refreshToken(accessToken, refreshToken, response);
        return ResponseEntity.ok(new Response("success"));
    }
}






