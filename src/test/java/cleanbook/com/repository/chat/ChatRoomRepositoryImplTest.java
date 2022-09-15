//package cleanbook.com.repository.chat;
//
//import cleanbook.com.dto.chat.ChatRoomDto;
//import cleanbook.com.entity.chat.ChatRoom;
//import cleanbook.com.repository.chatRoom.ChatRoomRepository;
//import cleanbook.com.repository.user.UserRepository;
//import cleanbook.com.service.ChatRoomService;
//import cleanbook.com.service.ChatService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class ChatRoomRepositoryImplTest {
//
//    @Autowired
//    private ChatRoomRepository chatRoomRepository;
//    @Autowired
//    private ChatRoomService chatRoomService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private ChatService chatService;
//
//
//    @Test
//    @DisplayName("채팅방 전체 조회")
//    void readChatRoomList() {
//
//        //given
//        ChatRoom chatRoom1 = chatRoomService.createChatRoom("채팅방1", Arrays.asList(1L, 2L, 3L));
//        ChatRoom chatRoom2 = chatRoomService.createChatRoom("채팅방2", Arrays.asList(1L, 3L));
//        ChatRoom chatRoom3 = chatRoomService.createChatRoom("채팅방3", Arrays.asList(1L, 2L));
//        chatService.createChat(chatRoom2.getId(), 1L , "안녕");
//        chatService.createChat(chatRoom3.getId(), 1L , "방가");
//        chatService.createChat(chatRoom1.getId(), 1L , "안녕");
//        chatService.createChat(chatRoom1.getId(), 2L , "ㅎㅇ");
//
//
//        // when
//        List<ChatRoomDto> chatRoomDtoList = chatRoomRepository.readChatRoomList(1L);
//        for (ChatRoomDto chatRoomDto : chatRoomDtoList) {
//            System.out.println("chatRoomDto.toString() = " + chatRoomDto.toString());
//        }
//        ChatRoomDto first = chatRoomDtoList.get(0);
//        ChatRoomDto second = chatRoomDtoList.get(1);
//
//
//        // then
//        assertThat(chatRoomDtoList.size()).isEqualTo(3);
//        assertThat(chatRoomDtoList.get(1).getHeadCount()).isEqualTo(2);
//
//        assertThat(first.getHeadCount()).isEqualTo(3);
//        assertThat(second.getLastChat()).isEqualTo("방가");
//        assertThat(first.getLastChat()).isEqualTo("ㅎㅇ");
//    }
//}
//
//
//
