package cleanbook.com.repository.user;

import cleanbook.com.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCommentRepository extends JpaRepository<User, Long> {

}
