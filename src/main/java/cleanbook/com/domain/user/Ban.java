package cleanbook.com.domain.user;

import cleanbook.com.domain.Timestamped;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Ban extends Timestamped {

    @Id
    @GeneratedValue
    @Column(name = "ban_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    void setUser(User user) {
        this.user = user;
        user.getBanUserList().add(this);
    }

    void setTargetUser(User user) {
        this.targetUser = user;
    }
}
