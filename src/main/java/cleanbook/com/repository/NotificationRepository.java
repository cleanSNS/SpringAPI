package cleanbook.com.repository;

import cleanbook.com.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByCreatedDateAfter(LocalDateTime date);
}
