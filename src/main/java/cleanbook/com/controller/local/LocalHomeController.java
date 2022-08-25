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

    // SSE 연결
    @GetMapping(value = "/test")
    public String home() {
        return "notification/notification";
    }

    // SSE 연결
    @GetMapping(value = "/test/{id}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long id,
                            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        return notificationService.subscribe(id, lastEventId);
    }

    // SSE 연결
    @ResponseBody
    @GetMapping(value = "/test/send", produces="application/json;charset=UTF-8")
    public String send() {
        User user = userRepository.findById(1L).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(2L).orElseThrow(UserNotFoundException::new);
        notificationService.send(user, targetUser, NotificationType.COMMENT);
        return "success";
    }
}
