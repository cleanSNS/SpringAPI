package cleanbook.com.service;

import cleanbook.com.entity.chat.Chat;
import cleanbook.com.entity.chat.ChatRoom;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.ChatRepository;
import cleanbook.com.repository.ChatRoomRepository;
import cleanbook.com.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository ChatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    // 모든 채팅방 보기
    public List<ChatRoom> findAllChatRoom() {
        return ChatRoomRepository.findAll();
    }

    // 채팅방 찾기
    public ChatRoom findChatRoomById(Long id) {
        return ChatRoomRepository.findById(id).orElseThrow();
    }

    // 채팅방 만들기
    public ChatRoom createChatRoom(String name) {
        return ChatRoomRepository.save(ChatRoom.createChatRoom(name));
    }

    /////////////////

    // 채팅 생성
    public Chat createChat(Long ChatRoomId, Long userId, String message) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        ChatRoom ChatRoom = ChatRoomRepository.findById(ChatRoomId).orElseThrow();  //방 찾기 -> 없는 방일 경우 여기서 예외처리
        return chatRepository.save(Chat.createChat(ChatRoom, user, message));
    }

    // 채팅방 채팅내용 불러오기
    public List<Chat> findAllChatByChatRoomId(Long ChatRoomId) {
        return chatRepository.findAllByChatRoomId(ChatRoomId);
    }


}
