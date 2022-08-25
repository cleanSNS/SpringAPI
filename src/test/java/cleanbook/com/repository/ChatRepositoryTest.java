package cleanbook.com.repository;

import cleanbook.com.config.QuerydslConfig;
import cleanbook.com.repository.chat.ChatRepository;
import cleanbook.com.repository.chatRoom.ChatRoomRepository;
import cleanbook.com.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

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



    }
}