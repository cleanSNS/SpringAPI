package cleanbook.com.repository.notification;

import cleanbook.com.config.QuerydslConfig;
import cleanbook.com.dto.NotificationDto;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.notification.Notification;
import cleanbook.com.entity.notification.NotificationType;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.UserProfile;
import cleanbook.com.exception.exceptions.MyException;
import cleanbook.com.exception.exceptions.NoMoreCommentException;
import cleanbook.com.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static cleanbook.com.dto.NotificationDto.createNotificationDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class NotificationRepositoryImplTest {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    private User myUser;

    @BeforeEach
    void init() {
        UserProfile userProfile = new UserProfile("name", 25, GenderType.FEMALE);
        User user = new User("aa", "aa", userProfile);
        User targetUser = new User("bb", "bb", userProfile);
        myUser = targetUser;

        userRepository.save(user);
        userRepository.save(targetUser);

        for (int i = 1; i <= 15; i++) {
            Notification notification = Notification.createNotification(user, targetUser, NotificationType.COMMENT, (long) i);
            notificationRepository.save(notification);
        }
    }


    @Nested
    @DisplayName("알림")
    class notification{

        @Test
        @DisplayName("알림 조회")
        void firstPage() {

            // when
            ResultDto<List<NotificationDto>> resultDto = notificationRepository.readNotificationList(myUser.getId(), 99L, 10);
            List<NotificationDto> notificationDtoList = resultDto.getData();
            Long startId = resultDto.getStartId();


            // then
            assertThat(notificationDtoList).extracting("resourceId").contains(15L,10L,6L);
        }

        @Test
        @DisplayName("두번째 페이지 알림 조회")
        void secondPage() {

            // when
            ResultDto<List<NotificationDto>> resultDto = notificationRepository.readNotificationList(myUser.getId(), 99L, 10);
            Long startId = resultDto.getStartId();

            resultDto = notificationRepository.readNotificationList(myUser.getId(), startId, 10);
            List<NotificationDto> notificationDtoList = resultDto.getData();


            // then
            assertThat(notificationDtoList).extracting("resourceId").contains(1L,2L,3L);
            assertThat(notificationDtoList.size()).isEqualTo(5);
        }

        @Test
        @DisplayName("더이상 조회할 알림이 없음")
        public void readCommentListThirdTest() {

            // when
            ResultDto<List<NotificationDto>> resultDto = notificationRepository.readNotificationList(myUser.getId(), 0L, 10);
            List<NotificationDto> notificationDtoList = resultDto.getData();
            Long startId = resultDto.getStartId();


            // then
            assertThat(notificationDtoList.size()).isEqualTo(0);
        }
    }

}