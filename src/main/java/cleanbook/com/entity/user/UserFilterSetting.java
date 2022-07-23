package cleanbook.com.entity.user;

import lombok.AllArgsConstructor;
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
public class UserFilterSetting {

    @Column(columnDefinition = "boolean default false")
    private boolean filterAll;

    @Column(columnDefinition = "boolean default false")
    private boolean filterFollower;

    @Column(columnDefinition = "boolean default false")
    private boolean filterFollowee;
}
