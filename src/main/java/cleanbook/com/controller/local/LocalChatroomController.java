package cleanbook.com.controller.local;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatroomDto;
import cleanbook.com.dto.chat.ChatroomForm;
import cleanbook.com.dto.chat.ChatroomUpdateDto;
import cleanbook.com.exception.Response;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/local")
@RequiredArgsConstructor
public class LocalChatroomController {

    private final ChatroomService chatRoomService;
    private final TokenProvider tokenProvider;

    // 채팅방 생성
    @PostMapping("/chat/chatroom")
    public ResponseEntity<Response> createChatroom(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody ChatroomForm chatRoomForm) {
        Long userId = tokenProvider.getUserId(token);
        chatRoomService.createChatroom(userId, chatRoomForm.getName(), chatRoomForm.getUserIdList());
        return ResponseEntity.ok(new Response("success"));
    }

    // 채팅방 전체 조회
    @GetMapping("/chat/chatroom")
    public ResultDto<List<ChatroomDto>> chatRoomList(@CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        return chatRoomService.readChatroomList(userId);
    }

    // 채팅방 이름 수정
    @PostMapping("/chat/chatroom/{chatroomId}")
    public ResponseEntity<Response> changeChatroomName(@CookieValue("X-AUTH-TOKEN") String token,
                                                       @PathVariable Long chatroomId, @RequestBody ChatroomUpdateDto dto) {
        Long userId = tokenProvider.getUserId(token);
        chatRoomService.changeName(userId, chatroomId, dto.getName());
        return ResponseEntity.ok(new Response("success"));
    }


    // 채팅방 삭제
    @DeleteMapping("/chat/chatroom/{chatroomId}")
    public ResponseEntity<Response> deleteChatroom(@CookieValue("X-AUTH-TOKEN") String token, @PathVariable Long chatroomId){
        Long userId = tokenProvider.getUserId(token);
        chatRoomService.deleteChatroom(userId, chatroomId);
        return ResponseEntity.ok(new Response("success"));
    }




}
