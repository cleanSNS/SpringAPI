package cleanbook.com.controller;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatDto;
import cleanbook.com.dto.chat.ChatMessage;
import cleanbook.com.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅 보내기
    @MessageMapping("/{roomId}") // /pub/1 여기로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    @SendTo("/sub/{roomId}")   // /sub/1 구독하고 있는 장소로 메시지 전송 (목적지)  -> WebSocketConfig Broker 에서 적용한건 앞에 붙어줘야됨
    public ChatMessage chat(@DestinationVariable Long roomId, ChatMessage message) {

        //채팅 저장
        chatService.createChat(roomId, Long.valueOf(message.getSender()), message.getMessage());
        return message;
    }

    // 전체 채팅 조회, 최신순으로 100개씩
    @GetMapping("/chat/{chatRoomId}")
    public ResultDto<List<ChatDto>> readChatList(@PathVariable Long chatRoomId,@RequestParam Long startId) {
        return chatService.readChatList(chatRoomId, startId);
    }
}
