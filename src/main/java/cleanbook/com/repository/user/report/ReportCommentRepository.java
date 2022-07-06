package cleanbook.com.repository.user.report;

import cleanbook.com.domain.user.User;
import cleanbook.com.domain.user.report.ReportComment;
import cleanbook.com.domain.user.report.ReportUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCommentRepository extends JpaRepository<ReportComment, Long> {

}
