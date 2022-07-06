package cleanbook.com.domain.user.block;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Block extends Timestamped {

    @Id
    @GeneratedValue
    @Column(name = "block_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    public Block(User user, User targetUser) {
        this.user = user;
        this.targetUser = targetUser;
    }

    public static Block createBlock(User user, User targetUser) {
        Block block = new Block(user, targetUser);
        user.getBlockUserList().add(block);
        return block;
    }
}
