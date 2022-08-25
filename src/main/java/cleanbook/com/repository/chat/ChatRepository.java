package cleanbook.com.repository.chat;

import cleanbook.com.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {
}
