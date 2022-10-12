package cleanbook.com.repository.chat;

import cleanbook.com.dto.chat.ChatroomDto;
import cleanbook.com.entity.chat.Chatroom;
import cleanbook.com.repository.chatroom.ChatroomRepository;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.service.ChatroomService;
import cleanbook.com.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChatroomRepositoryImplTest {

    @Autowired
    private ChatroomRepository chatRoomRepository;
    @Autowired
    private ChatroomService chatRoomService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatService chatService;


    @Test
    @DisplayName("채팅방 전체 조회")
    void readChatroomList() {

        //given
        Chatroom chatRoom1 = chatRoomService.createChatroom(1L,"채팅방1", Arrays.asList(1L, 2L, 3L));
        Chatroom chatRoom2 = chatRoomService.createChatroom(1L,"채팅방2", Arrays.asList(1L, 3L));
        Chatroom chatRoom3 = chatRoomService.createChatroom(1L,"채팅방3", Arrays.asList(1L, 2L));
        chatService.createChat(chatRoom2.getId(), "user1" , "안녕", LocalDateTime.now());
        chatService.createChat(chatRoom3.getId(), "user1" , "방가", LocalDateTime.now());
        chatService.createChat(chatRoom1.getId(), "user1" , "안녕", LocalDateTime.now());
        chatService.createChat(chatRoom1.getId(), "user2" , "ㅎㅇ", LocalDateTime.now());


        // when
        List<ChatroomDto> chatRoomDtoList = chatRoomRepository.readChatroomList(1L).getData();
        for (ChatroomDto chatRoomDto : chatRoomDtoList) {
            System.out.println("chatRoomDto.toString() = " + chatRoomDto.toString());
        }
        ChatroomDto first = chatRoomDtoList.get(0);
        ChatroomDto second = chatRoomDtoList.get(1);


        // then
        assertThat(chatRoomDtoList.size()).isEqualTo(3);
        assertThat(chatRoomDtoList.get(1).getHeadCount()).isEqualTo(2);

        assertThat(first.getHeadCount()).isEqualTo(3);
        assertThat(second.getLastChat()).isEqualTo("방가");
        assertThat(first.getLastChat()).isEqualTo("ㅎㅇ");
    }
}



