package cleanbook.com.domain.user.report;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.page.Comment;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReportComment extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "report_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_comment_id")
    private Comment targetComment;

    public static ReportComment createReportComment(User user, Comment targetComment) {
        ReportComment reportComment = new ReportComment();
        reportComment.user = user;
        reportComment.targetComment = targetComment;
        reportComment.report(targetComment);
        return reportComment;
    }

    public void report(Comment comment) {
        comment.reported();
    }

}
