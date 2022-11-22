package cleanbook.com.entity.page;

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
public class PageSetting {

    @Builder.Default
    private Boolean notificationLike = false;

    @Builder.Default
    private Boolean notificationComment = true;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private SettingType readAuth = SettingType.ALL;

    @Builder.Default
    private Boolean commentAuth = true;

    @Builder.Default
    private Boolean likeReadAuth = true;
}
