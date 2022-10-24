package cleanbook.com.service;

import cleanbook.com.dto.CountDto;
import cleanbook.com.dto.NotificationCountDto;
import cleanbook.com.dto.SSEType;
import cleanbook.com.dto.chat.ChatCountDto;
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
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cleanbook.com.dto.NotificationDto.*;
import static cleanbook.com.dto.SSEType.CHAT;
import static cleanbook.com.dto.SSEType.NOTIFICATION;
import static cleanbook.com.entity.notification.Notification.createNotification;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 5;

    private final Map<Long, SseEmitter> notificationEmitterMap = new ConcurrentHashMap<>();
    private final Map<Long, SseEmitter> chatEmitterMap = new ConcurrentHashMap<>();

    void print(String process) {
        log.info(process);
        for (Map.Entry<Long, SseEmitter> entrySet : notificationEmitterMap.entrySet()) {
            log.info("userId {}: emitter {}", entrySet.getKey(), entrySet.getValue());
        }
        log.info("");
    }

//    public SseEmitter subscribeNotification(Long userId) {
//        print("notification SSE 연결시작");
//
////        // 이미 sse연결이 되었을시 해제하고 재연결함
////        if (notificationEmitterMap.containsKey(userId)) {
////            log.info("notification SSE 연결 존재");
////            removeSseEmitter(userId);
////        }
//
//        if (notificationEmitterMap.containsKey(userId)) {
//            SseEmitter emitter = notificationEmitterMap.get(userId);
//            sendToClient(emitter, "EventStream Created. [userId=" + userId + "]");
//            return emitter;
//        }
//
//        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
//
//        emitter.onTimeout(()-> {
//            removeSseEmitter(userId);
//        });
//
//        notificationEmitterMap.put(userId, emitter);
//
//        print("notification SSE");
//
//        // 503 에러를 방지하기 위한 더미 이벤트 전송
//        sendToClient(emitter, "EventStream Created. [userId=" + userId + "]");
//
//        return emitter;
//    }
//
//    public SseEmitter subscribeChat(Long userId) {
//        print("connect start chat SSE");
//
////        // 이미 sse연결이 되었을시 해제하고 재연결함
////        if (chatEmitterMap.containsKey(userId)) {
////            log.info("chat SSE 연결 존재");
////            removeChatSseEmitter(userId);
////        }
//
//        if (chatEmitterMap.containsKey(userId)) {
//            SseEmitter emitter = chatEmitterMap.get(userId);
//            sendToClient(emitter, "EventStream Created. [userId=" + userId + "]");
//            return emitter;
//        }
//
//        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
//
//        emitter.onTimeout(()-> {
//            removeChatSseEmitter(userId);
//        });
//
//        chatEmitterMap.put(userId, emitter);
//
//        print("connect complete chat SSE");
//
//        // 503 에러를 방지하기 위한 더미 이벤트 전송
//        sendToClient(emitter, "EventStream Created. [userId=" + userId + "]");
//
//        return emitter;
//    }

    public SseEmitter subscribe(Long userId, SSEType sseType) {
        print(sseType.name() + " SSE connect start");

        Map<Long, SseEmitter> emitterMap = getEmitterMap(sseType);

        // 이미 sse연결이 되었을시 해제하고 재연결함
        if (emitterMap.containsKey(userId)) {
            log.info("{} SSE exist", sseType.name());
            removeSseEmitter(userId, sseType);
        }

//        if (emitterMap.containsKey(userId)) {
//            log.info("{} SSE exist", sseType.name());
//            SseEmitter emitter = emitterMap.get(userId);
//            return emitter;
//        }

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitter.onTimeout(()-> {
            removeSseEmitter(userId, sseType);
        });
        emitter.onCompletion(()-> {
            removeSseEmitter(userId, sseType);
        });

        emitterMap.put(userId, emitter);

        print(sseType.name() + " SSE connect complete");

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, "EventStream Created. [userId=" + userId + "]");

        return emitter;
    }

    private Map<Long, SseEmitter> getEmitterMap(SSEType sseType) {
        Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();
        switch (sseType) {
            case NOTIFICATION:
                emitterMap = notificationEmitterMap;
                break;
            case CHAT:
                emitterMap = chatEmitterMap;
                break;
            default:
                log.info("SSEType error");
                break;
        }
        return emitterMap;
    }

    public void removeSseEmitter(Long userId, SSEType sseType) {
        Map<Long, SseEmitter> emitterMap = getEmitterMap(sseType);
        SseEmitter emitter = emitterMap.get(userId);
        emitter.complete();
        emitterMap.remove(userId);
        print(sseType.name() + " SSE delete");
    }

    private void sendToClient(SseEmitter emitter, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            throw new MyException("SSE 연결 오류!");
        }
    }

    public void sendNotificationCount(Long receiver, Long count) {
        SseEmitter emitter = notificationEmitterMap.get(receiver);

        if (emitter != null) {
            sendToClient(emitter, new NotificationCountDto(count));
        }
    }

    public void sendUncheckedChatCount(Long receiver, Long count) {
        SseEmitter emitter = notificationEmitterMap.get(receiver);

        if (emitter != null) {
            log.info("로그인 상태인 사용자 : {}", receiver);
            sendToClient(emitter, new ChatCountDto(count));
        } else {
            log.info("로그아웃 상태인 사용자 : {}", receiver);
        }
    }

    public void sendChatNotification(Long receiver) {
        SseEmitter emitter = chatEmitterMap.get(receiver);

        if (emitter != null) {
            log.info("로그인 상태인 사용자 : {}", receiver);
            sendToClient(emitter, true);
        } else {
            log.info("로그아웃 상태인 사용자 : {}", receiver);
        }
    }
}
