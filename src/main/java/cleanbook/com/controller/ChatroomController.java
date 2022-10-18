package cleanbook.com.controller;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.*;
import cleanbook.com.dto.user.UserDto;
import cleanbook.com.exception.Response;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;
    private final TokenProvider tokenProvider;

    // 채팅방 생성
    @PostMapping("/chat/chatroom")
    public ResultDto<ChatroomIdDto> createChatroom(@CookieValue("X-AUTH-TOKEN") String token, @RequestBody ChatroomForm chatroomForm) {
        Long userId = tokenProvider.getUserId(token);
        Long id = chatroomService.createChatroom(userId, chatroomForm.getName(), chatroomForm.getUserIdList()).getId();
        return new ResultDto<>(new ChatroomIdDto(id));
    }

    // 채팅방 전체 조회
    @GetMapping("/chat/chatroom")
    public ResultDto<List<ChatroomDto>> chatroomList(@CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        return chatroomService.readChatroomList(userId);
    }

    // 채팅방 조회
    @GetMapping("/chat/chatroom/{chatroomId}")
    public ResultDto<ChatroomNameAndUserDto> getChatroomName(@PathVariable Long chatroomId) {
        return chatroomService.getChatroom(chatroomId);
    }

    // 채팅방 이름 수정
    @PostMapping("/chat/chatroom/{chatroomId}")
    public ResponseEntity<Response> changeChatroomName(@CookieValue("X-AUTH-TOKEN") String token,
                                                       @PathVariable Long chatroomId, @RequestBody ChatroomUpdateDto dto) {
        Long userId = tokenProvider.getUserId(token);
        chatroomService.changeName(userId, chatroomId, dto.getName());
        return ResponseEntity.ok(new Response("success"));
    }


    // 채팅방 삭제
    @DeleteMapping("/chat/chatroom/{chatroomId}")
    public ResponseEntity<Response> deleteChatroom(@CookieValue("X-AUTH-TOKEN") String token, @PathVariable Long chatroomId){
        Long userId = tokenProvider.getUserId(token);
        chatroomService.deleteChatroom(userId, chatroomId);
        return ResponseEntity.ok(new Response("success"));
    }



}
