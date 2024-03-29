package cleanbook.com.service;

import cleanbook.com.entity.chat.Chatroom;
import cleanbook.com.entity.chat.UserChatroom;
import cleanbook.com.exception.exceptions.NotFoundException;
import cleanbook.com.repository.chatroom.ChatroomRepository;
import cleanbook.com.repository.chatroom.UserChatroomRepository;
import cleanbook.com.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatroomServiceTest {

    @Autowired
    private ChatroomService chatroomService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private UserChatroomRepository userChatroomRepository;
    @Autowired
    private ChatService chatService;

    @Test
    @DisplayName("채팅방 생성")
    void noneFollowCreateChatroomTest() {

        //given
        // 유저 2명은 이미 저장되어있음


        // when
        chatroomService.createChatroom(1L,"내채팅방", Arrays.asList(1L,2L));


        // then
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        assertThat(chatroomList.size()).isEqualTo(1);
    }

    @Nested
    @DisplayName("채팅방 이름 수정")
    class changeChatroomName {

        @Test
        @DisplayName("존재하는 채팅방 이름 수정")
        void changeExistChatroomName() {

            //given
            // 유저 2명은 이미 저장되어있음


            // when
            Chatroom chatroom = chatroomService.createChatroom(1L,"내채팅방", Arrays.asList(1L, 2L));
            chatroomService.changeName(1L, chatroom.getId(), "채팅방");
            UserChatroom userChatroom = userChatroomRepository.findByUser_IdAndChatroom_Id(1L, chatroom.getId()).get();


            // then
            assertThat(userChatroom.getName()).isEqualTo("채팅방");
        }

        @Test
        @DisplayName("존재하지 않는 채팅방 이름 수정")
        void changeNotExistChatroomName() {

            //given
            // 유저 2명은 이미 저장되어있음


            // when
            chatroomService.createChatroom(1L,"내채팅방", Arrays.asList(1L, 2L));


            // then
            Throwable exception = assertThrows(NotFoundException.class, () ->
                    chatroomService.changeName(1L, 2L, "채팅방")
            );
            assertEquals("존재하지 않는 채팅방입니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("채팅방 삭제")
    class deleteChatroom {

        @Nested
        @DisplayName("존재하는 채팅방 삭제")
        class deleteExistChatroom {

            @Test
            @DisplayName("2명이상일 때")
            void goeTwo() {

                //given
                // 유저 2명은 이미 저장되어있음


                // when
                Chatroom chatroom = chatroomService.createChatroom(1L,"내채팅방", Arrays.asList(1L, 2L));
                chatService.createChat(chatroom.getId(), 1L , "ㅎㅇ", LocalDateTime.now());
                chatService.createChat(chatroom.getId(), 2L , "hi", LocalDateTime.now());

                chatroomService.deleteChatroom(1L, chatroom.getId());
                Chatroom newChatroom = chatroomRepository.findById(chatroom.getId()).get();


                // then
                assertThat(newChatroom.getUserChatroomList().size()).isEqualTo(1);
                assertThat(newChatroom.getUserChatroomList().get(0).getUser().getId()).isEqualTo(2L);
                assertThat(newChatroom.getChatList().size()).isEqualTo(2);
            }

            @Test
            @DisplayName("혼자 남았을 때")
            void alone() {

                //given
                // 유저 2명은 이미 저장되어있음


                // when
                Chatroom chatroom = chatroomService.createChatroom(1L,"내채팅방", Arrays.asList(1L, 2L));
                chatroomService.deleteChatroom(1L, chatroom.getId());
                chatroomService.deleteChatroom(2L, chatroom.getId());
                Optional<Chatroom> optionalChatroom = chatroomRepository.findById(1L);


                // then
                assertThat(optionalChatroom.isPresent()).isFalse();
            }
        }

        @Test
        @DisplayName("존재하지 않는 채팅방 삭제")
        void deleteNotExistChatroom() {

            //given
            // 유저 2명은 이미 저장되어있음


            // when
            chatroomService.createChatroom(1L,"내채팅방", Arrays.asList(1L, 2L));


            // then
            Throwable exception = assertThrows(NotFoundException.class, () ->
                    chatroomService.deleteChatroom(1L, 100L)
            );
            assertEquals("존재하지 않는 채팅방입니다.", exception.getMessage());
        }
    }

}





