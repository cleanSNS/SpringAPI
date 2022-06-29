package cleanbook.com.domain.notice;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private NoticeType type;

    private String url;

    private LocalDateTime readDate;
}
