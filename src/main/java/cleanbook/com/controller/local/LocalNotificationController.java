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

    // SSE 연결
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@CookieValue(value = "X-AUTH-TOKEN") String token,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        Long userId = tokenProvider.getUserId(token);
        return notificationService.subscribe(userId, lastEventId);
    }
}
