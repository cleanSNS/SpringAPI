package cleanbook.com.service;

import cleanbook.com.entity.chat.Chat;
import cleanbook.com.entity.chat.Chatroom;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.follow.Follow;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.FollowRepository;
import cleanbook.com.repository.chat.ChatRepository;
import cleanbook.com.repository.chatroom.ChatroomRepository;
import cleanbook.com.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChatServiceTest {

    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("채팅 생성")
    void createChat() {

        //given
        User user = userRepository.findById(1L).orElseThrow(UserNotFoundException::new);
        User user2 = userRepository.findById(2L).orElseThrow(UserNotFoundException::new);
        followRepository.save(new Follow(user,user2));
        followRepository.save(new Follow(user2,user));
        Chatroom chatroom = chatroomService.createChatroom(1L,"내채팅방", Arrays.asList(1L, 2L));


        // when
        Chat chat1 = chatService.createChat(chatroom.getId(), 1L, "ㅎㅇ", LocalDateTime.now());
        Chat chat = chatRepository.findById(chat1.getId()).get();


        // then
        assertThat(chat.getMessage()).isEqualTo("ㅎㅇ");

    }
}