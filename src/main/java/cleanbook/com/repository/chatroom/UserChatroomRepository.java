package cleanbook.com.repository.chatroom;

import cleanbook.com.entity.chat.UserChatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserChatroomRepository extends JpaRepository<UserChatroom, Long> {
    Optional<UserChatroom> findByUser_IdAndChatroom_Id(Long userId, Long chatroomId);
}
