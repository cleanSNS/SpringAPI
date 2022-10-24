package cleanbook.com.config;

import cleanbook.com.entity.chat.UserChatroom;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.NotFoundException;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.chatroom.ChatroomRepository;
import cleanbook.com.repository.chatroom.UserChatroomRepository;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    private final UserChatroomRepository userChatroomRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        log.info("채팅방 입장");
        Long chatroomId = getchatroomIdFromEvent(event);
        Long userId = Long.valueOf(event.getUser().getName());

        // 구독(채팅방 입장)시
        // 읽지 않은 채팅 수 0, 채팅방 입장 시간 업데이트, 읽지 않은 전체 채팅수 감소, 채팅방 입장 boolean true
        UserChatroom userChatroom = userChatroomRepository.findByUser_IdAndChatroom_Id(userId, chatroomId).orElseThrow(() -> new NotFoundException("채팅방"));
        userChatroom.resetUncheckedChatCount();
        userChatroom.subscribeChatroom();
        // 읽지 않은 전체 채팅수 알림
        notificationService.sendUncheckedChatCount(userId, getTotalUncheckedChatCount(userId));
        // 채팅방 업데이트를 위해
        notificationService.sendChatNotification(userId);
    }

    // subscribe로부터 채팅방 ID 가져오기
    public Long getchatroomIdFromEvent(SessionSubscribeEvent event) {
        String string = (Objects.requireNonNull(event.getMessage().getHeaders().get("nativeHeaders"))).toString();
        String roomIdStr = "";
        for (int i = string.lastIndexOf('/') + 1; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                roomIdStr += string.charAt(i);
            }
        }
        return Long.valueOf(roomIdStr);
    }

    public Long getTotalUncheckedChatCount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Long uncheckedChatCount = 0L;
        for (UserChatroom chatroom : user.getUserChatroomList()) {
            uncheckedChatCount += chatroom.getUncheckedChatCount();
        }
        return uncheckedChatCount;
    }
}
