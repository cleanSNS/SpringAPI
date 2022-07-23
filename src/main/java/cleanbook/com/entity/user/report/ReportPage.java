package cleanbook.com.entity.user.report;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReportPage extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_page_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_page_id")
    private Page targetPage;

    public static ReportPage createReportPage(User user, Page targetPage) {
        ReportPage reportPage = new ReportPage();
        reportPage.user = user;
        reportPage.targetPage = targetPage;
        reportPage.report(targetPage);
        return reportPage;
    }

    public void report(Page page) {
        page.reported();
    }
}
