package cleanbook.com.repository.user;

import cleanbook.com.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findUserByEmail(String email);
}
