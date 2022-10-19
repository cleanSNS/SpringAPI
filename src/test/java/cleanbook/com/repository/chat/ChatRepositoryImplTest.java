package cleanbook.com.repository.chat;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.chat.ChatDto;
import cleanbook.com.entity.chat.Chatroom;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.follow.Follow;
import cleanbook.com.exception.exceptions.NoMoreDataException;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.repository.FollowRepository;
import cleanbook.com.repository.chatroom.ChatroomRepository;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.service.ChatroomService;
import cleanbook.com.service.ChatService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
@Transactional
class ChatRepositoryImplTest {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowRepository followRepository;

    Long chatroomId;

    @BeforeEach
    void init() throws InterruptedException {
        User user = userRepository.findById(1L).orElseThrow(UserNotFoundException::new);
        User user2 = userRepository.findById(2L).orElseThrow(UserNotFoundException::new);
        User user3 = userRepository.findById(3L).orElseThrow(UserNotFoundException::new);
        followRepository.save(new Follow(user,user3));
        followRepository.save(new Follow(user3,user));
        chatroomId = chatroomService.createChatroom(1L, "채팅방", Arrays.asList(1L, 2L, 3L)).getId();
        for (int i = 0; i < 140; i++) {
            chatService.createChat(chatroomId, 1L, "ㅎㅇ" + i, LocalDateTime.now());
            // 동시에 생성되어 순서가 뒤집히는 경우가 있음
            Thread.sleep(10);
        }
    }


    @Nested
    @DisplayName("채팅방의 채팅 불러오기")
    class readChatList{

        @Test
        @DisplayName("채팅방의 채팅 불러오기")
        void readChatListFirstTest() {

            //given


            // 첫 100개
            // when
            ResultDto<List<ChatDto>> resultDto = chatRepository.readChatList(chatroomId, 999999L, 100);
            List<ChatDto> chatDtoList = resultDto.getData();
            for (ChatDto chatDto : chatDtoList) {
                System.out.println("chatDto.getMessage() = " + chatDto.getMessage());
            }
            Long startId = resultDto.getStartId();


            // then
            assertThat(chatDtoList.size()).isEqualTo(100);
            assertThat(chatDtoList.get(0).getMessage()).isEqualTo("ㅎㅇ139");
            assertThat(chatDtoList.get(chatDtoList.size()-1).getMessage()).isEqualTo("ㅎㅇ40");

        }

        @Test
        @DisplayName("2번째 페이지 채팅 불러오기")
        void readChatListSecondTest() {

            //given


            // 첫 100개
            // when
            ResultDto<List<ChatDto>> resultDto = chatRepository.readChatList(chatroomId, 999999L, 100);
            List<ChatDto> chatDtoList = resultDto.getData();
            Long startId = resultDto.getStartId();
            for (ChatDto chatDto : chatDtoList) {
                System.out.println("chatDto.getMessage() = " + chatDto.getMessage());
            }


            // 나머지 40개
            // when
            resultDto = chatRepository.readChatList(chatroomId, startId, 100);
            chatDtoList = resultDto.getData();
            startId = resultDto.getStartId();


            // then
            assertThat(chatDtoList.size()).isEqualTo(40);
            assertThat(chatDtoList.get(0).getMessage()).isEqualTo("ㅎㅇ39");
            assertThat(chatDtoList.get(chatDtoList.size()-1).getMessage()).isEqualTo("ㅎㅇ0");

        }

        @Test
        @DisplayName("더이상 채팅이 없음")
        void readChatListThirdTest() {

            //given


            // 첫 100개
            // when
            ResultDto<List<ChatDto>> resultDto = chatRepository.readChatList(chatroomId, 999999L, 100);
            List<ChatDto> chatDtoList = resultDto.getData();
            Long startId = resultDto.getStartId();

            // 나머지 40개
            // when
            resultDto = chatRepository.readChatList(chatroomId, startId, 100);
            chatDtoList = resultDto.getData();
            startId = resultDto.getStartId();

            // 더이상 채팅이 없음
            // when
            // then
            Long finalStartId = startId;
            resultDto = chatRepository.readChatList(chatroomId, finalStartId, 100);
            chatDtoList = resultDto.getData();
            assertThat(chatDtoList.size()).isEqualTo(0);
        }
    }


}



