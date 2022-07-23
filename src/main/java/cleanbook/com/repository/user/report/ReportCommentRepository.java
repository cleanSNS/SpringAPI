package cleanbook.com.repository.user.report;

import cleanbook.com.entity.user.report.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCommentRepository extends JpaRepository<ReportComment, Long> {

}
