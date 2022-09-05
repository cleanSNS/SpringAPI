package cleanbook.com.repository.user.report;

import cleanbook.com.entity.user.report.ReportPage;
import cleanbook.com.entity.user.report.ReportUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportPageRepository extends JpaRepository<ReportPage, Long> {
    Optional<ReportPage> findByUser_IdAndTargetPage_Id(Long userId, Long targetPageId);
}
