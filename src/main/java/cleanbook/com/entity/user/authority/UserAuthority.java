package cleanbook.com.entity.user.authority;

import cleanbook.com.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserAuthority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_authority_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_name")
    private Authority authority;

    public static UserAuthority createUserAuthority(User user, Authority authority) {
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.user = user;
        userAuthority.authority = authority;
        user.getUserAuthorityList().add(userAuthority);
        return userAuthority;
    }
}
