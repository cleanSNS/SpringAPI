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
    private UserNoticeSetting userNoticeSetting;

    @Embedded
    private UserFilterSetting userFilterSetting;

    public void changeUserNoticeSetting(UserNoticeSetting userNoticeSetting) {
        this.userNoticeSetting = userNoticeSetting;
    }

    public void changeUserFilterSetting(UserFilterSetting userFilterSetting) {
        this.userFilterSetting = userFilterSetting;
    }
}
