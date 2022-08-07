package cleanbook.com.dto;

import cleanbook.com.entity.notification.Notification;
import lombok.Data;

@Data
public class NotificationDto {
    private Long userId;
    private Long targetUserId;
    private String content;
    private String url;

    public static NotificationDto createNotificationDto(Notification notification) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.userId = notification.getUser().getId();
        notificationDto.targetUserId = notification.getTargetUser().getId();
        notificationDto.url = notification.getUrl();

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
        }
        return notificationDto;
    }
}
