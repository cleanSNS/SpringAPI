package cleanbook.com.dto.user;

import cleanbook.com.entity.user.UserFilterSetting;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class UserFilterSettingDto {
    private boolean filterAll;
    private boolean filterFollower;
    private boolean filterFollowee;

    public UserFilterSettingDto(UserFilterSetting userFilterSetting) {
        this.filterAll = userFilterSetting.getFilterAll();
        this.filterFollower = userFilterSetting.getFilterFollower();
        this.filterFollowee = userFilterSetting.getFilterFollowee();
    }
}
