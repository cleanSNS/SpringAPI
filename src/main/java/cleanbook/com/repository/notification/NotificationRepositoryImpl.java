package cleanbook.com.repository.notification;

import cleanbook.com.dto.CountDto;
import cleanbook.com.dto.Notification.NotificationDto;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.entity.notification.Notification;
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

    // 확인하지 않은 알림 갯수
    public ResultDto<CountDto> uncheckedNotificationCount(Long userId) {

        Long count = queryFactory.query()
                .select(notification.count())
                .from(notification)
                .where(notification.targetUser.id.eq(userId), notification.checked.isFalse())
                .fetchOne();

        return new ResultDto<>(new CountDto(count));
    }

    // 확인하지 않은 채팅 갯수
    public ResultDto<CountDto> uncheckedChatCount(Long userId) {

        Long count = queryFactory.query()
                .select(notification.count())
                .from(notification)
                .where(notification.targetUser.id.eq(userId), notification.checked.isFalse())
                .fetchOne();

        return new ResultDto<>(new CountDto(count));
    }
}
