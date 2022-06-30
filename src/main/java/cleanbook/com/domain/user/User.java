package cleanbook.com.domain.user;

import cleanbook.com.domain.notice.Notice;
import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.chat.UserChatRoom;
import cleanbook.com.domain.page.Page;
import lombok.Getter;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
public class User extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    @Embedded
    private UserProfile userProfile;

    @Column(columnDefinition = "integer default 0")
    private int warningCount;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'INACTIVE'")
    private AccountState accountState;

    @Embedded
    private UserSetting userSetting;

    @OneToMany(mappedBy = "user")
    private List<Page> pageList;

    @OneToMany(mappedBy = "targetUser")
    private List<Follow> followerList; // 나를 팔로우하는 사람

    @OneToMany(mappedBy = "user")
    private List<Follow> followeeList; // 내가 팔로우하는 사람

    @OneToMany(mappedBy = "user")
    private List<Ban> banUserList;

    @OneToMany(mappedBy = "user")
    private List<UserChatRoom> userChatRoomList;

    @OneToMany(mappedBy = "targetUser")
    private List<Notice> noticeList;

    void setEmail(String email) {
        this.email = email;
    }

    void setPassword(String password) {
        this.password = password;
    }

    void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    void setUserSetting(UserSetting userSetting) {
        this.userSetting = userSetting;
    }
}
