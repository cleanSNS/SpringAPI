package cleanbook.com.domain.user.report;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.page.Comment;
import cleanbook.com.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportUser extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "report_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    public ReportUser(User user, User targetUser) {
        this.user = user;
        this.targetUser = targetUser;
    }

}
