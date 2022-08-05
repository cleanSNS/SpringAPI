package cleanbook.com.controller.local;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/local")
public class LocalHomeController {

    @GetMapping("/test")
    public String hello() {
        return "hello";
    }

}
