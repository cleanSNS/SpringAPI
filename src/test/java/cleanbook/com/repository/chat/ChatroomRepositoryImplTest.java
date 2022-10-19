package cleanbook.com.repository.chat;

import cleanbook.com.dto.chat.ChatroomDto;
import cleanbook.com.entity.chat.Chatroom;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.follow.Follow;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.FollowRepository;
import cleanbook.com.repository.chatroom.ChatroomRepository;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.service.ChatroomService;
import cleanbook.com.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChatroomRepositoryImplTest {

    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatService chatService;
    @Autowired
    private FollowRepository followRepository;


    @Test
    @DisplayName("채팅방 전체 조회")
    void readChatroomList() {

        //given
        User user = userRepository.findById(1L).orElseThrow(UserNotFoundException::new);
        User user3 = userRepository.findById(3L).orElseThrow(UserNotFoundException::new);
        followRepository.save(new Follow(user,user3));
        followRepository.save(new Follow(user3,user));

        Chatroom chatroom1 = chatroomService.createChatroom(1L,"채팅방1", Arrays.asList(1L, 2L, 3L));
        Chatroom chatroom2 = chatroomService.createChatroom(1L,"채팅방2", Arrays.asList(1L, 3L));
        Chatroom chatroom3 = chatroomService.createChatroom(1L,"채팅방3", Arrays.asList(1L, 2L));
        chatService.createChat(chatroom2.getId(), 1L , "안녕", LocalDateTime.now());
        chatService.createChat(chatroom3.getId(), 1L , "방가", LocalDateTime.now());
        chatService.createChat(chatroom1.getId(), 1L , "안녕", LocalDateTime.now());
        chatService.createChat(chatroom1.getId(), 2L , "ㅎㅇ", LocalDateTime.now());


        // when
        List<ChatroomDto> chatroomDtoList = chatroomRepository.readChatroomList(1L).getData();
        for (ChatroomDto chatroomDto : chatroomDtoList) {
            System.out.println("chatroomDto.toString() = " + chatroomDto.toString());
        }
        ChatroomDto first = chatroomDtoList.get(0);
        ChatroomDto second = chatroomDtoList.get(1);


        // then
        assertThat(chatroomDtoList.size()).isEqualTo(3);
        assertThat(chatroomDtoList.get(1).getHeadCount()).isEqualTo(2);

        assertThat(first.getHeadCount()).isEqualTo(3);
        assertThat(second.getLastChat()).isEqualTo("방가");
        assertThat(first.getLastChat()).isEqualTo("ㅎㅇ");
    }
}



