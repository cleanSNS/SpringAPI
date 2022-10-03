package cleanbook.com.controller.local;

import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/local")
public class LocalNotificationController {

    private final NotificationService notificationService;
    private final TokenProvider tokenProvider;

    // SSE 테스트 페이지
    @GetMapping(value = "/test")
    public String home() {
        return "notification/notification";
    }

    // SSE 연결
    @GetMapping(value = "/test/{id}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long id,
                                @RequestParam("last_event_id") String lastEventId) {
        System.out.println("sse 연결");
        System.out.println("lastEventId = " + lastEventId);
        return notificationService.subscribe(id, lastEventId);
    }

    // SSE 연결
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@CookieValue(value = "X-AUTH-TOKEN") String token,
                                @RequestParam("last_event_id") String lastEventId) {

        Long userId = tokenProvider.getUserId(token);
        return notificationService.subscribe(userId, lastEventId);
    }
}
