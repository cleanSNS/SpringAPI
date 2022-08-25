package cleanbook.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/test")
    public String hello() {
        return "hello";
    }

    @GetMapping("/myroom/test")
    public String test() {
        return "chat/room";
    }
}
