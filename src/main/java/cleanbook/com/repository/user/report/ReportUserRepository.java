package cleanbook.com.repository.user.report;

import cleanbook.com.entity.user.report.ReportUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportUserRepository extends JpaRepository<ReportUser, Long> {
    Optional<ReportUser> findByUser_IdAndTargetUser_Id(Long userId, Long targetUserId);
}
