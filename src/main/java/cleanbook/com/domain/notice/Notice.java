package cleanbook.com.domain.notice;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Getter
public class Notice extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "notice_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    private NoticeType type;

    @NotEmpty
    private String url;

    @Column(columnDefinition = "timestamp default 'null'")
    private LocalDateTime readDate;

    void setUser(User user) {
        this.user = user;
        targetUser.getNoticeList().add(this);
    }

    void setTargetUser(User user) {
        this.targetUser = user;
    }

    void setContents(NoticeType type, String url, LocalDateTime readDate) {
        this.type = type;
        this.url = url;
        this.readDate = readDate;
    }
}
