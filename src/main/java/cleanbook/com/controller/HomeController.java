package cleanbook.com.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/test")
    public String hello() {
        return "hello";
    }

    @GetMapping("/test2")
    public String hello2() {
        System.out.println("principal " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "1";
    }
}
