package cleanbook.com.domain.user;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@DynamicInsert
@Embeddable
public class UserFilterSetting {

    @Column(columnDefinition = "boolean default false")
    private boolean filterAll;

    @Column(columnDefinition = "boolean default false")
    private boolean filterFollower;

    @Column(columnDefinition = "boolean default false")
    private boolean filterFollowee;
}
