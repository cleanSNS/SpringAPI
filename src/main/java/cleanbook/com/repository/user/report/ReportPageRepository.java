package cleanbook.com.repository.user.report;

import cleanbook.com.domain.user.report.ReportComment;
import cleanbook.com.domain.user.report.ReportPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportPageRepository extends JpaRepository<ReportPage, Long> {

}
