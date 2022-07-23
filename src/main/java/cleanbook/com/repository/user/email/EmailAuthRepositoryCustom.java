package cleanbook.com.repository.user.email;

import cleanbook.com.entity.user.EmailAuth;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailAuthRepositoryCustom {
    Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime currentTime);
}
