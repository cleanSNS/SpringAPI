package cleanbook.com.entity.user.filter;

import cleanbook.com.entity.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "filter_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    public Filter(User user, User targetUser) {
        this.user = user;
        this.targetUser = targetUser;
    }

    public static Filter createFilter(User user, User targetUser) {
        Filter filter = new Filter(user, targetUser);
        user.getNotFilterUserList().add(filter);
        return filter;
    }
}
