package cleanbook.com.repository;

import cleanbook.com.entity.user.UserActive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserActiveRepository extends JpaRepository<UserActive, String> {
    Optional<UserActive> findByEmail(String email);
}
