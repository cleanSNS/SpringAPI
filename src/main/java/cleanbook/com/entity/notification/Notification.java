package cleanbook.com.entity.notification;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import cleanbook.com.validation.ValidEnum;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @ValidEnum(enumClass = NotificationType.class)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @NotBlank
    private String url;

    @Column(columnDefinition = "timestamp default null")
    private LocalDateTime readDate;

    public static Notification createNotification(User sender, User receiver, NotificationType type) {
        Notification notification = new Notification();
        notification.user = sender;
        notification.targetUser = receiver;
        notification.type = type;
        notification.url = "url";
        return notification;
    }
}
