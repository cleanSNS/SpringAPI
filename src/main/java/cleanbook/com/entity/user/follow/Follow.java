package cleanbook.com.entity.user.follow;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Follow extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    public Follow(User user, User targetUser) {
        this.user = user;
        this.targetUser = targetUser;
    }

    public static Follow createFollow(User user, User targetUser) {
        Follow follow = new Follow();
        follow.user = user;
        user.getFolloweeList().add(follow);
        follow.targetUser = targetUser;
        targetUser.getFollowerList().add(follow);
        return follow;
    }

}
