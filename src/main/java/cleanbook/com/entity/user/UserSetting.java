package cleanbook.com.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
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
}
