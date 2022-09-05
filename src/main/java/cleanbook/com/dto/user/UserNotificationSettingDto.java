package cleanbook.com.dto.user;

import cleanbook.com.entity.enums.SettingType;
import cleanbook.com.entity.user.UserNotificationSetting;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserNotificationSettingDto {

    private boolean notificationFollow;
    private SettingType notificationComment;
    private SettingType notificationLike;
    private boolean notificationFollowAccept;
    private boolean notificationChat;

    public UserNotificationSettingDto(UserNotificationSetting userNotificationSetting) {
        this.notificationFollow = userNotificationSetting.isNotificationFollow();
        this.notificationComment = userNotificationSetting.getNotificationComment();
        this.notificationLike = userNotificationSetting.getNotificationLike();
        this.notificationFollowAccept = userNotificationSetting.isNotificationFollowAccept();
        this.notificationChat = userNotificationSetting.isNotificationChat();
    }
}
