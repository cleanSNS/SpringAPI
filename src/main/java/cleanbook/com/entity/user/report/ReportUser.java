package cleanbook.com.entity.user.report;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportUser extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    public static ReportUser createReportUser(User user, User targetUser) {
        ReportUser reportUser = new ReportUser();
        reportUser.user = user;
        reportUser.targetUser = targetUser;
        reportUser.report(targetUser);
        return reportUser;
    }

    public void report(User user) {
        user.reported();
    }

}
