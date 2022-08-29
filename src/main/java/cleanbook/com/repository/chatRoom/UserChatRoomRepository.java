package cleanbook.com.repository.chatRoom;

import cleanbook.com.entity.chat.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
    Optional<UserChatRoom> findByUser_IdAndChatRoom_Id(Long userId, Long chatRoomId);
}
