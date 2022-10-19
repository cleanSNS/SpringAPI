package cleanbook.com.service;

import cleanbook.com.entity.chat.Chat;
import cleanbook.com.entity.chat.Chatroom;
import cleanbook.com.repository.chat.ChatRepository;
import cleanbook.com.repository.chatroom.ChatroomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    @DisplayName("채팅 생성")
    void createChat() {

        //given
        Chatroom chatroom = chatroomService.createChatroom(1L,"내채팅방", Arrays.asList(1L, 2L));


        // when
        Chat chat1 = chatService.createChat(chatroom.getId(), 1L, "ㅎㅇ", LocalDateTime.now());
        Chat chat = chatRepository.findById(chat1.getId()).get();


        // then
        assertThat(chat.getMessage()).isEqualTo("ㅎㅇ");

    }
}