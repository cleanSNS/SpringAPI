package cleanbook.com.controller.local;

import cleanbook.com.dto.ChatMessage;
import cleanbook.com.dto.ChatRoomForm;
import cleanbook.com.entity.chat.Chat;
import cleanbook.com.entity.chat.ChatRoom;
import cleanbook.com.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class LocalChatRoomController {

    private final ChatService chatService;

    /**
     * 채팅방 참여하기
     * @param roomId 채팅방 id
     */
    @GetMapping("/room/{roomId}")
    public String joinRoom(@PathVariable(required = false) Long roomId, Model model) {
        List<Chat> chatList = chatService.findAllChatByChatRoomId(roomId);

        List<ChatMessage> chatMessageList = chatList.stream()
                .map(ChatMessage::new)
                .collect(Collectors.toList());

        for (ChatMessage chatMessage : chatMessageList) {
            System.out.println("chatMessage = " + chatMessage);
        }

        model.addAttribute("roomId", roomId);
        model.addAttribute("chatList", chatMessageList);
        return "chat/room";
    }

    /**
     * 채팅방 등록
     * @param form
     */
    @PostMapping("/room")
    public String createChatRoom(ChatRoomForm form) {
        chatService.createChatRoom(form.getName());
        return "redirect:/roomList";
    }

    /**
     * 채팅방 리스트 보기
     */
    @GetMapping("/roomList")
    public String roomList(Model model) {
        List<ChatRoom> roomList = chatService.findAllChatRoom();
        model.addAttribute("roomList", roomList);
        return "chat/roomList";
    }

    /**
     * 방만들기 폼
     */
    @GetMapping("/roomForm")
    public String roomForm() {
        return "chat/roomForm";
    }

}
