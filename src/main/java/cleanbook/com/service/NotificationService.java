package cleanbook.com.service;

import cleanbook.com.entity.notification.Notification;
import cleanbook.com.entity.notification.NotificationType;
import cleanbook.com.entity.user.User;
import cleanbook.com.repository.NotificationRepository;
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

    public SseEmitter subscribe(Long userId, String lastEventId) {

        // DB에는 소수점 6자리까지밖에 저장이 되지않아 미리 포메팅 해줌
        String id = userId + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterMap.put(userId, emitter);

        emitter.onCompletion(() -> emitterMap.remove(userId));
        emitter.onTimeout(() -> emitterMap.remove(userId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created. [userId=" + userId + "]");

        log.info("lastEventId {}", lastEventId);
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.equals("")){
            String getTime = lastEventId.substring(lastEventId.indexOf('_') + 1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            LocalDateTime lastConnectionTime = LocalDateTime.parse(getTime, formatter);

            List<Notification> notificationList = notificationRepository.findAllByCreatedDateAfter(lastConnectionTime);
            for (Notification notification : notificationList) {
                sendToClient(emitter, id, createNotificationDto(notification));
            }
        }

        return emitter;
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

    public void send(User sender, User receiver, NotificationType type) {
        Notification notification = createNotification(sender, receiver, type);
        notificationRepository.save(notification);

        String id = receiver.getId() + "_" + notification.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));;

        SseEmitter emitter = emitterMap.get(receiver.getId());

        sendToClient(emitter, id, createNotificationDto(notification));
    }



}
