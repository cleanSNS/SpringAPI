package cleanbook.com.controller;

import cleanbook.com.dto.chat.ChatRoomDto;
import cleanbook.com.dto.chat.ChatRoomForm;
import cleanbook.com.dto.chat.ChatRoomUpdateDto;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final TokenProvider tokenProvider;

    // 채팅방 생성
    @PostMapping("/chatRoom")
    public void createChatRoom(@RequestBody ChatRoomForm chatRoomForm) {
        chatRoomService.createChatRoom(chatRoomForm.getName(), chatRoomForm.getUserIdList());
    }

    // 채팅방 전체 조회
    @GetMapping("/chatRoom")
    public List<ChatRoomDto> chatRoomList(@CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        return chatRoomService.readChatRoomList(userId);
    }

    // 채팅방 이름 수정
    @PostMapping("/chatRoom/{chatRoomId}")
    public void changeChatRoomName(@PathVariable Long chatRoomId, @RequestBody ChatRoomUpdateDto dto) {
        chatRoomService.changeName(chatRoomId, dto.getName());
    }


    // 채팅방 삭제
    @DeleteMapping("/chatRoom/{chatRoomId}")
    public void deleteChatRoom(@CookieValue("X-AUTH-TOKEN") String token, @PathVariable Long chatRoomId){
        Long userId = tokenProvider.getUserId(token);
        chatRoomService.deleteChatRoom(userId, chatRoomId);
    }




}
