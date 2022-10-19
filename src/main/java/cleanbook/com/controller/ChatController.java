package cleanbook.com.controller;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatDto;
import cleanbook.com.dto.chat.ChatMessage;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.service.ChatService;
import cleanbook.com.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final TokenProvider tokenProvider;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    // 채팅 보내기
    @MessageMapping("/{chatroomId}") // /pub/1 여기로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    @SendTo("/sub/{chatroomId}")   // /sub/1 구독하고 있는 장소로 메시지 전송 (목적지)  -> WebSocketConfig Broker 에서 적용한건 앞에 붙어줘야됨
    public ChatMessage chat(@DestinationVariable Long chatroomId, ChatMessage message) {
        log.info("채팅 생성");

        // 채팅 저장
        chatService.createChat(chatroomId, message.getUserId(), message.getMessage(), message.getCreatedDate());
        return message;
    }

    // 전체 채팅 조회, 최신순으로 100개씩
    @GetMapping("/chat/{chatroomId}")
    public ResultDto<List<ChatDto>> readChatList(@CookieValue("X-AUTH-TOKEN") String token,
                                                 @PathVariable Long chatroomId,@RequestParam Long startId) {
        Long userId = tokenProvider.getUserId(token);
        return chatService.readChatList(userId, chatroomId, startId);
    }

    @MessageMapping("/hello")
    public void message(Message message) {
        simpMessageSendingOperations.convertAndSend("/sub/" + message.getChannelId(), message);
    }
}
