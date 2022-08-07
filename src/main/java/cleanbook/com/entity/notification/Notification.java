package cleanbook.com.entity.notification;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Getter
public class Notification extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @NotEmpty
    private String url;

    @Column(columnDefinition = "timestamp default null")
    private LocalDateTime readDate;

    void setUser(User user) {
        this.user = user;
        targetUser.getNotificationList().add(this);
    }

    void setTargetUser(User user) {
        this.targetUser = user;
    }

    void setContents(NotificationType type, String url, LocalDateTime readDate) {
        this.type = type;
        this.url = url;
        this.readDate = readDate;
    }
}
