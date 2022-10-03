package cleanbook.com.controller.local;

import cleanbook.com.entity.notification.NotificationType;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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


}
