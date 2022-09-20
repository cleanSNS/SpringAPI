package cleanbook.com.repository.notification;

import cleanbook.com.dto.NotificationDto;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.entity.notification.Notification;
import cleanbook.com.exception.exceptions.MyException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cleanbook.com.entity.notification.QNotification.notification;


@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    // 알림내역 조회
    public ResultDto<List<NotificationDto>> readNotificationList(Long userId, Long startId, int pageSize) {

        // nested false인 댓글을 10개씩
        List<Notification> notificationList = queryFactory.query()
                .select(notification)
                .from(notification)
                .where(notification.targetUser.id.eq(userId), notification.id.loe(startId))
                .limit(pageSize)
                .orderBy(notification.createdDate.desc())
                .fetch();

        if (notificationList.isEmpty()) {
            return new ResultDto<>(new ArrayList<>(), 0L);
        }

        List<NotificationDto> notificationDtoList = notificationList.stream()
                .map(NotificationDto::createNotificationDto)
                .collect(Collectors.toList());

        Long nextStartId = notificationList.get(notificationList.size()-1).getId()-1;

        return new ResultDto<>(notificationDtoList, nextStartId);
    }
}
