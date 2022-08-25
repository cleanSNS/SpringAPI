package cleanbook.com.service;

import cleanbook.com.dto.chat.ChatRoomDto;
import cleanbook.com.entity.chat.ChatRoom;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.NotFoundException;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.chatRoom.ChatRoomRepository;
import cleanbook.com.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // 채팅방 생성(서로 팔로우 중인 경우에만 가능)
    public ChatRoom createChatRoom(String name, List<Long> userIdList) {
        List<User> userList = new ArrayList<>();
        for (Long id : userIdList) {
            userList.add(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
        }
        return chatRoomRepository.save(ChatRoom.createChatRoom(name, userList));
    }

    // 채팅방 전체 조회
    public List<ChatRoomDto> readChatRoomList(Long userId, Long startId) {
        return chatRoomRepository.readChatRoomList(userId);
    }

    // 채팅방 이름수정
    public void changeName(Long id, String name) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(() -> new NotFoundException("채팅방"));
        chatRoom.changeName(name);
    }

    //채팅방 삭제(나가기)
    public void deleteChatRoom(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(() -> new NotFoundException("채팅방"));
        chatRoomRepository.delete(chatRoom);
    }

}
