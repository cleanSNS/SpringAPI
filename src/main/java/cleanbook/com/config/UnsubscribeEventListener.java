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
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Objects;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class UnsubscribeEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final UserChatroomRepository userChatroomRepository;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        log.info("채팅방 퇴장");
        System.out.println(event);
        System.out.println(event.getMessage());
        System.out.println(event.getUser());
        Long chatroomId = getchatroomIdFromEvent(event);
        Long userId = Long.valueOf(event.getUser().getName());
        System.out.println("chatroomId = " + chatroomId);
        System.out.println("userId = " + userId);

        // 구독(채팅방 입장)시
        UserChatroom userChatroom = userChatroomRepository.findByUser_IdAndChatroom_Id(userId, chatroomId).orElseThrow(() -> new NotFoundException("채팅방"));
        userChatroom.unsubscribeChatroom();
    }

    // subscribe로부터 채팅방 ID 가져오기
    public Long getchatroomIdFromEvent(SessionDisconnectEvent event) {
        String string = (Objects.requireNonNull(event.getMessage().getHeaders().get("nativeHeaders"))).toString();
        String roomIdStr = "";
        for (int i = string.lastIndexOf('/') + 1; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                roomIdStr += string.charAt(i);
            }
        }
        return Long.valueOf(roomIdStr);
    }

}
