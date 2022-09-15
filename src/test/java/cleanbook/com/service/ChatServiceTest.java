//package cleanbook.com.service;
//
//import cleanbook.com.entity.chat.Chat;
//import cleanbook.com.entity.chat.ChatRoom;
//import cleanbook.com.repository.chat.ChatRepository;
//import cleanbook.com.repository.chatRoom.ChatRoomRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class ChatServiceTest {
//
//    @Autowired
//    private ChatService chatService;
//    @Autowired
//    private ChatRoomService chatRoomService;
//    @Autowired
//    private ChatRepository chatRepository;
//    @Autowired
//    private ChatRoomRepository chatRoomRepository;
//
//    @Test
//    @DisplayName("채팅 생성")
//    void createChat() {
//
//        //given
//        ChatRoom chatRoom = chatRoomService.createChatRoom("내채팅방", Arrays.asList(1L, 2L));
//
//
//        // when
//        chatService.createChat(chatRoom.getId(), 1L, "ㅎㅇ");
//        Chat chat = chatRepository.findById(1L).get();
//
//
//        // then
//        assertThat(chat.getMessage()).isEqualTo("ㅎㅇ");
//
//    }
//}