package cleanbook.com.entity.user;

import cleanbook.com.entity.enums.AccountState;
import cleanbook.com.entity.notification.Notification;
import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.chat.UserChatRoom;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.user.authority.UserAuthority;
import cleanbook.com.entity.user.block.Block;
import cleanbook.com.entity.user.filter.Filter;
import cleanbook.com.entity.user.follow.Follow;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    @Embedded
    private UserProfile userProfile;

    @Embedded
    private UserSetting userSetting;

    @Column(columnDefinition = "integer default 0")
    private int warningCount;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'INACTIVE'")
    private AccountState accountState;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAuthority> userAuthorityList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Page> pageList = new ArrayList<>();

    @OneToMany(mappedBy = "targetUser")
    private List<Follow> followerList = new ArrayList<>(); // 나를 팔로우하는 사람

    @OneToMany(mappedBy = "user")
    private List<Follow> followeeList = new ArrayList<>(); // 내가 팔로우하는 사람

    @OneToMany(mappedBy = "user")
    private List<Filter> notFilterUserList = new ArrayList<>(); // 내가 필터링하지 않을 사람

    @OneToMany(mappedBy = "user")
    private List<Block> blockUserList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserChatRoom> userChatRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "targetUser")
    private List<Notification> notificationList = new ArrayList<>();

    public User(String email, String password, UserProfile userProfile) {
        this.email = email;
        this.password = password;
        this.userProfile = userProfile;
    }

    @Builder
    public User(String email, String password, UserProfile userProfile,  UserSetting userSetting, AccountState accountState) {
        this.email = email;
        this.password = password;
        this.userProfile = userProfile;
        this.userSetting = userSetting;
        this.accountState = accountState;
    }

    public User(Long id, String email, String password, UserProfile userProfile) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userProfile = userProfile;
    }

    public User(Long id, String email, String password, UserProfile userProfile, UserSetting userSetting) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userProfile = userProfile;
        this.userSetting = userSetting;
    }

    public void reported() {
        this.warningCount++;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void changeUserNotificationSetting(UserNotificationSetting userNotificationSetting) {
        this.userSetting.changeUserNotificationSetting(userNotificationSetting);
    }

    public void changeUserFilterSetting(UserFilterSetting userFilterSetting) {
        this.userSetting.changeUserFilterSetting(userFilterSetting);
    }

    public void activateAccount() {
        this.accountState = AccountState.ACTIVE;
    }
}
