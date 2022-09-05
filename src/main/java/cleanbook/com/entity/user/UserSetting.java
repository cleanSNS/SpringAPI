package cleanbook.com.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Embeddable
@NoArgsConstructor
public class UserSetting {

    @Embedded
    private UserNotificationSetting userNotificationSetting;

    @Embedded
    private UserFilterSetting userFilterSetting;

    public void changeUserNotificationSetting(UserNotificationSetting userNotificationSetting) {
        this.userNotificationSetting = userNotificationSetting;
    }

    public void changeUserFilterSetting(UserFilterSetting userFilterSetting) {
        this.userFilterSetting = userFilterSetting;
    }

    @Builder
    public UserSetting(UserNotificationSetting userNotificationSetting, UserFilterSetting userFilterSetting) {
        this.userNotificationSetting = userNotificationSetting;
        this.userFilterSetting = userFilterSetting;
    }
}
