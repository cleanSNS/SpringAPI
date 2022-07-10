package cleanbook.com.domain.user;

import cleanbook.com.domain.Timestamped;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taget_user_id")
    private User targetUser;

    public Follow(User user, User targetUser) {
        this.user = user;
        user.getFolloweeList().add(this);
        this.targetUser = targetUser;
        targetUser.getFollowerList().add(this);
    }

//    void setUser(User user) {
//        this.user = user;
//        user.getFolloweeList().add(this);
//    }
//
//    void setTargetUser(User user) {
//        this.targetUser = user;
//        user.getFolloweeList().add(this);
//    }
}
