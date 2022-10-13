package cleanbook.com.controller.local;

import cleanbook.com.exception.Response;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/local")
public class LocalHomeController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @ResponseBody
    @GetMapping("/check_auth")
    public String hello() {
        return "hello";
    }

    @GetMapping("/testaa/{userId}")
    public ResponseEntity<Response> test(@PathVariable Long userId) {
        notificationService.removeSseEmitter(userId);
        return ResponseEntity.ok(new Response("success"));
    }
}
