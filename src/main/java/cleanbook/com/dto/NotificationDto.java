package cleanbook.com.dto;

import cleanbook.com.entity.notification.Notification;
import cleanbook.com.entity.notification.NotificationType;
import lombok.Data;

@Data
public class NotificationDto {
    private Long notificationId;
    private Long userId;
    private String userImgUrl;
    private Long targetUserId;
    private String content;
    private NotificationType type;
    private Long resourceId;
    private boolean checked;

    public static NotificationDto createNotificationDto(Notification notification) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.notificationId = notification.getId();
        notificationDto.userId = notification.getUser().getId();
        notificationDto.userImgUrl = notification.getUser().getUserProfile().getImgUrl();
        notificationDto.targetUserId = notification.getTargetUser().getId();
        notificationDto.type = notification.getType();
        notificationDto.resourceId = notification.getResourceId();
        notificationDto.checked = notification.isChecked();

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
