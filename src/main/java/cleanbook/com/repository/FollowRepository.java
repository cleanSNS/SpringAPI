package cleanbook.com.repository;

import cleanbook.com.entity.user.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
