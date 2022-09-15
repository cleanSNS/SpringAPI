//package cleanbook.com.service;
//
//import cleanbook.com.entity.chat.ChatRoom;
//import cleanbook.com.exception.exceptions.NotFoundException;
//import cleanbook.com.repository.chatRoom.ChatRoomRepository;
//import cleanbook.com.repository.user.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class ChatRoomServiceTest {
//
//    @Autowired
//    private ChatRoomService chatRoomService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private ChatRoomRepository chatRoomRepository;
//    @Autowired
//    private ChatService chatService;
//
//    @Test
//    @DisplayName("채팅방 생성")
//    void noneFollowCreateChatRoomTest() {
//
//        //given
//        // 유저 2명은 이미 저장되어있음
//
//
//        // when
//        chatRoomService.createChatRoom("내채팅방", Arrays.asList(1L,2L));
//
//
//        // then
//        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
//        assertThat(chatRoomList.size()).isEqualTo(1);
//        assertThat(chatRoomList.get(0).getName()).isEqualTo("내채팅방");
//    }
//
//    @Nested
//    @DisplayName("채팅방 이름 수정")
//    class changeChatRoomName {
//
//        @Test
//        @DisplayName("존재하는 채팅방 이름 수정")
//        void changeExistChatRoomName() {
//
//            //given
//            // 유저 2명은 이미 저장되어있음
//
//
//            // when
//            ChatRoom chatRoom = chatRoomService.createChatRoom("내채팅방", Arrays.asList(1L, 2L));
//            chatRoomService.changeName(chatRoom.getId(), "채팅방");
//            ChatRoom changedChatRoom = chatRoomRepository.findById(chatRoom.getId()).get();
//
//
//            // then
//            assertThat(changedChatRoom.getName()).isEqualTo("채팅방");
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 채팅방 이름 수정")
//        void changeNotExistChatRoomName() {
//
//            //given
//            // 유저 2명은 이미 저장되어있음
//
//
//            // when
//            chatRoomService.createChatRoom("내채팅방", Arrays.asList(1L, 2L));
//
//
//            // then
//            Throwable exception = assertThrows(NotFoundException.class, () ->
//                    chatRoomService.changeName(2L, "채팅방")
//            );
//            assertEquals("존재하지 않는 채팅방입니다.", exception.getMessage());
//        }
//    }
//
//    @Nested
//    @DisplayName("채팅방 삭제")
//    class deleteChatRoom {
//
//        @Nested
//        @DisplayName("존재하는 채팅방 삭제")
//        class deleteExistChatRoom {
//
//            @Test
//            @DisplayName("2명이상일 때")
//            void goeTwo() {
//
//                //given
//                // 유저 2명은 이미 저장되어있음
//
//
//                // when
//                ChatRoom chatRoom = chatRoomService.createChatRoom("내채팅방", Arrays.asList(1L, 2L));
//                chatService.createChat(chatRoom.getId(), 1L, "ㅎㅇ");
//                chatService.createChat(chatRoom.getId(), 2L, "hi");
//                chatRoomService.deleteChatRoom(1L, chatRoom.getId());
//                ChatRoom newChatRoom = chatRoomRepository.findById(chatRoom.getId()).get();
//
//
//                // then
//                assertThat(newChatRoom.getUserChatRoomList().size()).isEqualTo(1);
//                assertThat(newChatRoom.getUserChatRoomList().get(0).getUser().getId()).isEqualTo(2L);
//                assertThat(newChatRoom.getChatList().size()).isEqualTo(2);
//            }
//
//            @Test
//            @DisplayName("혼자 남았을 때")
//            void alone() {
//
//                //given
//                // 유저 2명은 이미 저장되어있음
//
//
//                // when
//                ChatRoom chatRoom = chatRoomService.createChatRoom("내채팅방", Arrays.asList(1L, 2L));
//                chatRoomService.deleteChatRoom(1L, chatRoom.getId());
//                Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(1L);
//
//
//                // then
//                assertThat(optionalChatRoom.isPresent()).isFalse();
//            }
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 채팅방 삭제")
//        void deleteNotExistChatRoom() {
//
//            //given
//            // 유저 2명은 이미 저장되어있음
//
//
//            // when
//            chatRoomService.createChatRoom("내채팅방", Arrays.asList(1L, 2L));
//
//
//            // then
//            Throwable exception = assertThrows(NotFoundException.class, () ->
//                    chatRoomService.deleteChatRoom(1L, 100L)
//            );
//            assertEquals("존재하지 않는 채팅방입니다.", exception.getMessage());
//        }
//    }
//
//}
//
//
//
//
//
