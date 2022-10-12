package cleanbook.com.controller;

import cleanbook.com.entity.notification.NotificationType;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.Response;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @ResponseBody
    @GetMapping("/check_auth")
    public String hello() {
        return "hello";
    }


}

