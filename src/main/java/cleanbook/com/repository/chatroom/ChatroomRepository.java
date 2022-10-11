package cleanbook.com.repository.chatroom;

import cleanbook.com.entity.chat.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long>, cleanbook.com.repository.chatroom.ChatroomRepositoryCustom {
}
