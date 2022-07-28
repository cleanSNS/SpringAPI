package cleanbook.com.controller.local;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/local")
public class LocalHomeController {

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
