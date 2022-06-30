package cleanbook.com.domain.user;

import cleanbook.com.domain.Timestamped;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Table(name = "likes")
public class Like extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    @Enumerated(EnumType.STRING)
    @NotEmpty
    private LikeType type;

    void setUser(User user) {
        this.user = user;
    }

    void setTargetUser(User user) {
        this.targetUser = user;
    }

    void setLikeType(LikeType type) {
        this.type = type;
    }
}
