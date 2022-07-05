package cleanbook.com.domain.user;

import javax.persistence.*;

@Embeddable
public class UserSetting {

    @Embedded
    private UserNoticeSetting userNoticeSetting;

    @Embedded
    private UserFilterSetting userFilterSetting;
}
