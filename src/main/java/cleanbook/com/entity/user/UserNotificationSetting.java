package cleanbook.com.entity.user;

import cleanbook.com.dto.user.UserNotificationSettingDto;
import cleanbook.com.entity.enums.SettingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@DynamicInsert
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotificationSetting {

    @Builder.Default
    private boolean notificationFollow = true;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private SettingType notificationComment = SettingType.ALL;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private SettingType notificationLike = SettingType.ALL;

    @Builder.Default
    private boolean notificationFollowAccept = true;

    @Builder.Default
    private boolean notificationChat = true;

    public static UserNotificationSetting createUserNotificationSetting(UserNotificationSettingDto dto) {
        return UserNotificationSetting.builder()
                .notificationFollow(dto.isNotificationFollow())
                .notificationComment(dto.getNotificationComment())
                .notificationLike(dto.getNotificationLike())
                .notificationFollowAccept(dto.isNotificationFollowAccept())
                .notificationChat(dto.isNotificationChat())
                .build();
    }
}
