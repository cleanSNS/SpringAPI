package cleanbook.com.repository;

import cleanbook.com.domain.user.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
