package cleanbook.com.repository;

import cleanbook.com.config.QuerydslConfig;
import cleanbook.com.entity.chat.Chat;
import cleanbook.com.entity.chat.ChatRoom;
import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.UserProfile;
import cleanbook.com.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class ChatRepositoryTest {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("채팅방의 채팅내역 보기")
    void findAllByChatRoomIdTest() {

        //given
        UserProfile userProfile = new UserProfile("a", 10, GenderType.FEMALE);
        User user = new User("aa", "aa", userProfile);
        userRepository.save(user);
        ChatRoom chatRoom = ChatRoom.createChatRoom("채팅방");
        chatRoomRepository.save(chatRoom);
        Chat chat = Chat.createChat(chatRoom, user, "hi");
        chatRepository.save(chat);
        Chat chat2 = Chat.createChat(chatRoom, user, "hii");
        chatRepository.save(chat2);


        // when
        List<Chat> chatList = chatRepository.findAllByChatRoomId(1L);
        for (Chat chat1 : chatRepository.findAll()) {
            System.out.println("chat1.getId() = " + chat1.getMessage());
        }

        // then
        assertThat(chatRepository.findAll().size()).isEqualTo(2);
        assertThat(chatList.size()).isEqualTo(2);

    }
}