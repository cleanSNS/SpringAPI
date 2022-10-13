package cleanbook.com.service;

import cleanbook.com.dto.CountDto;
import cleanbook.com.entity.notification.Notification;
import cleanbook.com.entity.notification.NotificationType;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.MyException;
import cleanbook.com.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cleanbook.com.dto.NotificationDto.*;
import static cleanbook.com.entity.notification.Notification.createNotification;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 30;

    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    void print(String process) {
        log.info(process);
        for (Map.Entry<Long, SseEmitter> entrySet : emitterMap.entrySet()) {
            log.info("userId {}: emitter {}", entrySet.getKey(), entrySet.getValue());
        }
        log.info("");
    }

    public SseEmitter subscribe(Long userId) {
        print("SSE 연결시작");

        // 이미 sse연결이 되었을시 해제하고 재연결함
        if (emitterMap.containsKey(userId)) {
            log.info("SSE 연결 존재");
            removeSseEmitter(userId);
        }

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterMap.put(userId, emitter);

        print("SSE 연결완료");

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, "EventStream Created. [userId=" + userId + "]");

        return emitter;
    }

    public void removeSseEmitter(Long userId) {
        SseEmitter emitter = emitterMap.get(userId);
        emitter.complete();
        emitterMap.remove(userId);
        print("SSE 삭제완료");
    }

    private void sendToClient(SseEmitter emitter, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            throw new RuntimeException("연결 오류!");
        }
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            throw new RuntimeException("연결 오류!");
        }
    }

    public void send(User sender, User receiver, NotificationType type, Long resourceId) {
        Notification notification = createNotification(sender, receiver, type, resourceId);
        notificationRepository.save(notification);

        String id = receiver.getId() + "_" + notification.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));;

        SseEmitter emitter = emitterMap.get(receiver.getId());

        if (emitter != null) {
            sendToClient(emitter, id, createNotificationDto(notification));
        }
    }

    public void sendNotificationCount(User receiver, Long count) {

        SseEmitter emitter = emitterMap.get(receiver.getId());

        if (emitter != null) {
            sendToClient(emitter, new CountDto(count));
        }
    }


}
