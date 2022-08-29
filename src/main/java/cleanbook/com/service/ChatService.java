package cleanbook.com.service;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatDto;
import cleanbook.com.entity.chat.Chat;
import cleanbook.com.entity.chat.ChatRoom;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.NotFoundException;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.chat.ChatRepository;
import cleanbook.com.repository.chatRoom.ChatRoomRepository;
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

    // 채팅 생성
    public Chat createChat(Long ChatRoomId, Long userId, String message) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        ChatRoom ChatRoom = ChatRoomRepository.findById(ChatRoomId).orElseThrow(() -> new NotFoundException("채팅방"));
        return chatRepository.save(Chat.createChat(ChatRoom, user, message));
    }

    // 채팅방 채팅내용 불러오기, 100개씩 최근순으로
    public ResultDto<List<ChatDto>> readChatList(Long chatRoomId, Long startId) {
        return chatRepository.readChatList(chatRoomId, startId, 100);
    }

}
