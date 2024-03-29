package cleanbook.com.repository.notification;

import cleanbook.com.dto.CountDto;
import cleanbook.com.dto.Notification.NotificationDto;
import cleanbook.com.dto.ResultDto;

import java.util.List;

public interface NotificationRepositoryCustom {
    ResultDto<List<NotificationDto>> readNotificationList(Long userId, Long startId, int pageSize);
    ResultDto<CountDto> uncheckedNotificationCount(Long userId);
}
