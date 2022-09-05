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

    @Column(columnDefinition = "boolean default false")
    private boolean notificationFollow;

    @Column(columnDefinition = "varchar(15) default 'ALL'")
    @Enumerated(EnumType.STRING)
    private SettingType notificationComment;

    @Column(columnDefinition = "varchar(15) default 'ALL'")
    @Enumerated(EnumType.STRING)
    private SettingType notificationLike;

    @Column(columnDefinition = "boolean default false")
    private boolean notificationFollowAccept;

    @Column(columnDefinition = "boolean default false")
    private boolean notificationChat;

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
