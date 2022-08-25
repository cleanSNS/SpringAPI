package cleanbook.com.controller.local;

import cleanbook.com.dto.chat.ChatMessage;
import cleanbook.com.entity.chat.Chat;
import cleanbook.com.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class LocalChatRoomControllerForTest {

    private final ChatService chatService;

    /**
     * 채팅방 참여하기
     * @param roomId 채팅방 id
     */
//    @GetMapping("/room/{roomId}")
//    public String joinRoom(@PathVariable(required = false) Long roomId, Model model) {
//        List<Chat> chatList = chatService.findAllChatByChatRoomId(roomId);
//
//        List<ChatMessage> chatMessageList = chatList.stream()
//                .map(ChatMessage::new)
//                .collect(Collectors.toList());
//
//        System.out.println("채팅방 참여");
//
//        model.addAttribute("roomId", roomId);
//        model.addAttribute("chatList", chatMessageList);
//        return "chat/room";
//    }

    /**
     * 채팅방 등록
     * @param form
     */
//    @PostMapping("/room")
//    public String createChatRoom(ChatRoomForm form) {
//        chatService.createChatRoom(form.getName());
//        return "redirect:/roomList";
//    }

    /**
     * 방만들기 폼
     */
    @GetMapping("/roomForm")
    public String roomForm() {
        return "chat/roomForm";
    }

}
