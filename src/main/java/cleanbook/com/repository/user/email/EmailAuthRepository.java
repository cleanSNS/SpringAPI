package cleanbook.com.repository.user.email;

import cleanbook.com.entity.user.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long>, EmailAuthRepositoryCustom{
}
