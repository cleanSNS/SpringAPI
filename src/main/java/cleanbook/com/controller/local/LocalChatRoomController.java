package cleanbook.com.controller.local;

import cleanbook.com.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/local")
@RequiredArgsConstructor
public class LocalChatRoomController {

    private final ChatService chatService;


//    // 채팅방 생성
//    @PostMapping("/room")
//    public void createChatRoom(ChatRoomForm chatRoomForm) {
//        chatService.createChatRoom(chatRoomForm.getName(), chatRoomForm.getUserIdList());
//    }
//
//    // 채팅방 삭제
//
//    // 채팅방 전체 조회
//   @GetMapping("/roomList")
//    public String roomList(Model model) {
//        List<ChatRoom> roomList = chatService.findAllChatRoom();
//        model.addAttribute("roomList", roomList);
//        return "chat/roomList";
//    }

    // 채팅방 이름 수정

}
