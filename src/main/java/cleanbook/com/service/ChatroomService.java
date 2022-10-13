package cleanbook.com.service;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatroomDto;
import cleanbook.com.entity.chat.Chatroom;
import cleanbook.com.entity.chat.UserChatroom;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.MyException;
import cleanbook.com.exception.exceptions.NotFoundException;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.chatroom.ChatroomRepository;
import cleanbook.com.repository.chatroom.UserChatroomRepository;
import cleanbook.com.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final UserRepository userRepository;
    private final UserChatroomRepository userChatroomRepository;

    // 채팅방 생성(서로 팔로우 중인 경우에만 가능)
    public Chatroom createChatroom(Long userId, String name, List<Long> userIdList) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<User> userList = new ArrayList<>();
        for (Long id : userIdList) {
            userList.add(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
        }
        return chatroomRepository.save(Chatroom.createChatroom(name, userList));
    }

    // 채팅방 전체 조회
    public ResultDto<List<ChatroomDto>> readChatroomList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return chatroomRepository.readChatroomList(userId);
    }

    // 채팅방 이름수정
    public void changeName(Long userId, Long chatroomId, String name) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Chatroom chatroom = chatroomRepository.findById(chatroomId).orElseThrow(() -> new NotFoundException("채팅방"));
        chatroom.changeName(name);
    }

    //채팅방 삭제(나가기)
    public void deleteChatroom(Long userId, Long chatroomId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Chatroom chatroom = chatroomRepository.findById(chatroomId).orElseThrow(() -> new NotFoundException("채팅방"));

        // 혼자 남았을 경우
        if (chatroom.getUserChatroomList().size() == 1) {
            chatroomRepository.delete(chatroom);
        } else {
            UserChatroom userChatroom = userChatroomRepository.findByUser_IdAndChatroom_Id(userId, chatroomId).orElseThrow(() -> new NotFoundException("채팅방"));
            chatroom.getUserChatroomList().remove(userChatroom);
            userChatroomRepository.delete(userChatroom);
        }

    }

    public String getChatroomName(Long chatroomId) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId).orElseThrow(() -> new NotFoundException("채팅방"));
        return chatroom.getName();
    }
}
