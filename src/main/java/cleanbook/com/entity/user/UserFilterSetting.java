package cleanbook.com.entity.user;

import cleanbook.com.dto.user.UserFilterSettingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@DynamicInsert
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilterSetting {

    @Builder.Default
    private boolean filterAll = true;

    @Builder.Default
    private boolean filterFollower = true;

    @Builder.Default
    private boolean filterFollowee = true;

    public static UserFilterSetting createUserFilterSetting(UserFilterSettingDto dto) {
        return UserFilterSetting.builder()
                .filterAll(dto.isFilterAll())
                .filterFollower(dto.isFilterFollower())
                .filterFollowee(dto.isFilterFollowee())
                .build();
    }
}
