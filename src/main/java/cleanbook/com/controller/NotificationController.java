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
    @GetMapping(value = "/test/{id}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long id) {
        System.out.println("sse 연결");
        return notificationService.subscribe(id);
    }

    // SSE 연결
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@CookieValue(value = "X-AUTH-TOKEN") String token) {

        Long userId = tokenProvider.getUserId(token);
        return notificationService.subscribe(userId);
    }
}
