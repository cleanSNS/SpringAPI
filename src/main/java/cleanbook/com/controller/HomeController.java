package cleanbook.com.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Value("${spring.social.kakao.redirect}")
    private String kakaoClientId;

    @GetMapping("/test")
    public String hello() {
        System.out.println("Test " + kakaoClientId);
        return "hello";
    }

    @GetMapping("/test2")
    public String hello2() {
        System.out.println("principal " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "1";
    }
}
