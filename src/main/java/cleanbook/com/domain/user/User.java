package cleanbook.com.domain.user;

import cleanbook.com.domain.notice.Notice;
import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.chat.UserChatRoom;
import cleanbook.com.domain.page.Page;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
public class User extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String name;
    private int warningCount;

    @Enumerated(value = EnumType.STRING)
    private AccountState accountState;

    private String imgUrl;
    private int age;
    private int gender;
    private String selfIntroduce;

    @Embedded
    private UserSetting userSetting;

    @OneToMany(mappedBy = "user")
    private List<Page> pageList;

    @OneToMany
    private List<User> followerList; // 나를 팔로우하는 사람

    @OneToMany
    private List<User> followeeList; // 내가 팔로우하는 사람

    @OneToMany
    private List<User> banUserList;

    @OneToMany(mappedBy = "user")
    private List<UserChatRoom> userChatRoomList;

    @OneToMany(mappedBy = "targetUser")
    private List<Notice> noticeList;

}
