package cleanbook.com.domain.user.report;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportPage extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "report_page_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_page_id")
    private Page targetPage;

    public ReportPage(User user, Page targetPage) {
        this.user = user;
        this.targetPage = targetPage;
    }

}
