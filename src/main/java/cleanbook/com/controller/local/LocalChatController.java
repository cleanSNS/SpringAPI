package cleanbook.com.controller.local;

import cleanbook.com.dto.chat.ChatMessage;
import cleanbook.com.entity.chat.Chat;
import cleanbook.com.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class LocalChatController {

    private final ChatService chatService;

    @MessageMapping("/{roomId}") // /send/1 여기로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    @SendTo("/room/{roomId}")   // /room/1 구독하고 있는 장소로 메시지 전송 (목적지)  -> WebSocketConfig Broker 에서 적용한건 앞에 붙어줘야됨
    public ChatMessage chat(@DestinationVariable Long roomId, ChatMessage message) {

        //채팅 저장
        Chat chat = chatService.createChat(roomId, Long.valueOf(message.getSender()), message.getMessage());
        return message;
    }

}
