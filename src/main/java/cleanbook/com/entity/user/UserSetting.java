package cleanbook.com.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Embeddable
@NoArgsConstructor
@Builder
public class UserSetting {

    @Builder.Default
    @Embedded
    private UserNotificationSetting userNotificationSetting = UserNotificationSetting.builder().build();

    @Builder.Default
    @Embedded
    private UserFilterSetting userFilterSetting = UserFilterSetting.builder().build();

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
