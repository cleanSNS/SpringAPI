package cleanbook.com.repository;

import cleanbook.com.entity.user.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByUser_IdAndTargetUser_Id(Long userId, Long targetUserId);
}
