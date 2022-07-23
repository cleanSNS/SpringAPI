package cleanbook.com.controller;

import cleanbook.com.dto.user.UserDeleteDto;
import cleanbook.com.dto.user.UserLoginDto;
import cleanbook.com.dto.user.UserSignUpDto;
import cleanbook.com.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserSignUpDto> signUp(@Valid @RequestBody UserSignUpDto userSignUpDto) {
        return ResponseEntity.ok(userService.signUp(userSignUpDto));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserLoginDto> login(@Valid @RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
        return ResponseEntity.ok(userService.login(userLoginDto, response));
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response){
        userService.logout(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원탈퇴
    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@Valid @RequestBody UserDeleteDto userDeleteDto, HttpServletResponse response) {
        userService.delete(userDeleteDto, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // accessToken refresh
    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue(name = "X-AUTH-TOKEN") String accessToken,
                                        @CookieValue(name = "REFRESH-TOKEN") String refreshToken,
                                        HttpServletResponse response) {
        userService.refresh(accessToken, refreshToken, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}






