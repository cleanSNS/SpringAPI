package cleanbook.com.domain.user;

import cleanbook.com.domain.Timestamped;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUser extends Timestamped {

    @Id
    @GeneratedValue
    @Column(name = "user_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taget_user_id")
    private User targetUser;

    @Enumerated(EnumType.STRING)
    private RelationType type;

    public UserUser(User user, User targetUser, RelationType type) {
        this.user = user;
        this.targetUser = targetUser;
        this.type = type;

        switch (type) {
            case FOLLOW:
                user.getFolloweeList().add(this);
                targetUser.getFollowerList().add(this);

            case BAN:
                user.getBanUserList().add(this);

            case REPORT:
        }
    }
}
