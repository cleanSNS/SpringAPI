package cleanbook.com.controller.local;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatDto;
import cleanbook.com.dto.chat.ChatMessage;
import cleanbook.com.entity.chat.Chat;
import cleanbook.com.service.ChatService;
import cleanbook.com.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/local")
@RequiredArgsConstructor
public class LocalChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    // 전체 채팅 조회, 최신순으로 100개씩
    @GetMapping("/chat/{chatRoomId}")
    public ResultDto<List<ChatDto>> readChatList(@PathVariable Long chatRoomId,@RequestParam Long startId) {
        return chatService.readChatList(chatRoomId, startId);
    }


    @MessageMapping("/hello")
    public void message(Message message) {
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), message);
    }
}
