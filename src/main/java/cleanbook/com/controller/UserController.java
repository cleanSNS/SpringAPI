package cleanbook.com.controller;

import cleanbook.com.domain.user.UserDeleteDto;
import cleanbook.com.domain.user.UserDto;
import cleanbook.com.domain.user.UserLoginDto;
import cleanbook.com.domain.user.UserSignUpDto;
import cleanbook.com.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response){
        userService.logout(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@Valid @RequestBody UserDeleteDto userDeleteDto, HttpServletResponse response) {
        userService.delete(userDeleteDto, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}






