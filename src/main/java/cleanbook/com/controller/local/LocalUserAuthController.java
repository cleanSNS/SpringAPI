package cleanbook.com.controller.local;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.user.*;
import cleanbook.com.exception.Response;
import cleanbook.com.jwt.TokenProvider;
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
    private final TokenProvider tokenProvider;

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
    public ResultDto<UserIdDto> login(@Validated @RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
        return new ResultDto<>(userAuthService.loginLocal(userLoginDto, response));
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletResponse response){
        userAuthService.logout(response);
        return ResponseEntity.ok(new Response("success"));
    }

    // 회원탈퇴
    @PostMapping("/delete")
    public ResponseEntity<Response> delete(@CookieValue("X-AUTH-TOKEN") String accessToken,
                                           HttpServletResponse response) {
        Long userId = tokenProvider.getUserId(accessToken);
        userAuthService.delete(userId, response);
        return ResponseEntity.ok(new Response("success"));
    }

    // refresh accessToken
    @GetMapping("/refresh")
    public ResponseEntity<Response> refresh(@CookieValue("X-AUTH-TOKEN") String accessToken,
                                            @RequestHeader("Authorization") String refreshToken,
                                        HttpServletResponse response) {

        userAuthService.refreshTokenLocal(accessToken, refreshToken, response);
        return ResponseEntity.ok(new Response("success"));
    }
}






