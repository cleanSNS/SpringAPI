package cleanbook.com.repository.user.report;

import cleanbook.com.entity.user.report.ReportComment;
import cleanbook.com.entity.user.report.ReportPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportCommentRepository extends JpaRepository<ReportComment, Long> {
    Optional<ReportComment> findByUser_IdAndTargetComment_Id(Long userId, Long targetCommentId);
}
