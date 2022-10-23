package cleanbook.com.service;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatDto;
import cleanbook.com.entity.chat.Chat;
import cleanbook.com.entity.chat.Chatroom;
import cleanbook.com.entity.chat.UserChatroom;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.MyException;
import cleanbook.com.exception.exceptions.NotFoundException;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.chat.ChatRepository;
import cleanbook.com.repository.chatroom.ChatroomRepository;
import cleanbook.com.repository.chatroom.UserChatroomRepository;
import cleanbook.com.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatroomRepository chatroomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final UserChatroomRepository userChatroomRepository;
    private final NotificationService notificationService;

    // 채팅 생성
    public Chat createChat(Long chatroomId, Long userId, String message, LocalDateTime createdDate) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Chatroom chatroom = chatroomRepository.findById(chatroomId).orElseThrow(() -> new NotFoundException("채팅방"));

        // 해당 채팅방에 속하지 않은 유저가 채팅을 보낼시 에러 발생
        if (userChatroomRepository.findByUser_IdAndChatroom_Id(user.getId(), chatroomId).isEmpty()) {
            throw new MyException("해당 채팅방에 속하지 않는 유저입니다.");
        }

        // 해당 채팅방에 속한 유저들에게 알림, 읽지 않은 채팅수 & 읽지 않은 전체 채팅수 + 1
        for (UserChatroom userChatroom : chatroom.getUserChatroomList()) {
            User targetUser = userChatroom.getUser();
            // 채팅 생성 알림, 마지막 채팅 트리거를 위해
            notificationService.sendChatNotification(targetUser.getId());
            // 읽지 않은 채팅수 & 읽지 않은 전체 채팅수 + 1
            if (!targetUser.getId().equals(user.getId())) {
                userChatroom.addUncheckedChatCount();
                notificationService.sendUncheckedChatCount(targetUser.getId(), getTotalUncheckedChatCount(targetUser.getId()));
            }
        }

        return chatRepository.save(Chat.createChat(chatroom, user, message, createdDate));
    }

    // 채팅방 채팅내용 불러오기, 100개씩 최근순으로
    public ResultDto<List<ChatDto>> readChatList(Long userId, Long chatroomId, Long startId) {
        // 채팅방의 참여자가 아닐경우 확인 불가
        if (userChatroomRepository.findByUser_IdAndChatroom_Id(userId, chatroomId).isEmpty()) {
            throw new MyException("해당 채팅방에 속하지 않는 유저입니다.");
        }
        return chatRepository.readChatList(chatroomId, startId, 100);
    }

    public Long getTotalUncheckedChatCount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Long uncheckedChatCount = 0L;
        for (UserChatroom chatroom : user.getUserChatroomList()) {
            uncheckedChatCount += chatroom.getUncheckedChatCount();
        }
        return uncheckedChatCount;
    }
}
