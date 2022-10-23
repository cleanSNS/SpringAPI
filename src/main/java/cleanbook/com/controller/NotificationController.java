package cleanbook.com.controller;

import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final TokenProvider tokenProvider;

    // SSE 테스트 페이지
    @GetMapping(value = "/test")
    public String home() {
        return "notification/notification";
    }

    // SSE 연결
    @GetMapping(value = "/subscribe/notification", produces = "text/event-stream")
    public SseEmitter subscribeNotification(@CookieValue(value = "X-AUTH-TOKEN") String token) {

        Long userId = tokenProvider.getUserId(token);
        return notificationService.subscribeNotification(userId);
    }

    // SSE 연결
    @GetMapping(value = "/subscribe/chat", produces = "text/event-stream")
    public SseEmitter subscribeChat(@CookieValue(value = "X-AUTH-TOKEN") String token) {

        Long userId = tokenProvider.getUserId(token);
        return notificationService.subscribeChat(userId);
    }
}
