package cleanbook.com.entity.user;

import cleanbook.com.entity.enums.AccountState;
import cleanbook.com.entity.notification.Notification;
import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.chat.UserChatroom;
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
@Builder
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;

    @Embedded
    @Builder.Default
    private UserProfile userProfile = UserProfile.builder().build();

    @Embedded
    @Builder.Default
    private UserSetting userSetting = UserSetting.builder().build();

    @Column(columnDefinition = "integer default 0")
    private int warningCount;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'INACTIVE'")
    private AccountState accountState;

    @Column(columnDefinition = "integer default 0")
    private int followerCount;

    @Column(columnDefinition = "integer default 0")
    private int followeeCount;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAuthority> userAuthorityList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Page> pageList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "targetUser", cascade = CascadeType.ALL)
    private List<Follow> followerList = new ArrayList<>(); // 나를 팔로우하는 사람

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Follow> followeeList = new ArrayList<>(); // 내가 팔로우하는 사람

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Filter> notFilterUserList = new ArrayList<>(); // 내가 필터링하지 않을 사람

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Block> blockedUserList = new ArrayList<>(); // 내가 차단한 사람

    @Builder.Default
    @OneToMany(mappedBy = "targetUser", cascade = CascadeType.ALL)
    private List<Block> blockUserList = new ArrayList<>(); // 나를 차단한 사람

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserChatroom> userChatroomList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "targetUser", cascade = CascadeType.ALL)
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

    @Builder
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

    public void follow(Follow follow) {
        this.getFolloweeList().add(follow);
        this.followeeCount++;
    }

    public void followed(Follow follow) {
        this.getFollowerList().add(follow);
        this.followerCount++;
    }

    public void unfollow(Follow follow) {
        this.getFolloweeList().remove(follow);
        this.followeeCount--;
    }

    public void unfollowed(Follow follow) {
        this.getFollowerList().remove(follow);
        this.followerCount--;
    }
}
