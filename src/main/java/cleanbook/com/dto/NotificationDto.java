package cleanbook.com.dto;

import cleanbook.com.entity.notification.Notification;
import cleanbook.com.entity.notification.NotificationType;
import lombok.Data;

@Data
public class NotificationDto {
    private Long userId;
    private Long targetUserId;
    private String content;
    private NotificationType type;
    private Long resourceId;

    public static NotificationDto createNotificationDto(Notification notification) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.userId = notification.getUser().getId();
        notificationDto.targetUserId = notification.getTargetUser().getId();
        notificationDto.type = notification.getType();
        notificationDto.resourceId = notification.getResourceId();

        switch (notification.getType()) {
            case COMMENT:
                notificationDto.content = "새로운 댓글이 달렸습니다.";
                break;

            case NESTED:
                notificationDto.content = "새로운 대댓글이 달렸습니다.";
                break;

            case FOLLOW:
                notificationDto.content = notification.getUser().getUserProfile().getNickname()+"님이 팔로우했습니다.";
                break;

            case LIKE:
                notificationDto.content = notification.getUser().getUserProfile().getNickname()+"님이 좋아합니다.";
                break;
        }
        return notificationDto;
    }
}
