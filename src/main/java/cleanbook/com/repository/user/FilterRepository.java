package cleanbook.com.repository.user;

import cleanbook.com.entity.user.filter.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilterRepository extends JpaRepository<Filter, Long> {
    Optional<Filter> findByUser_IdAndTargetUser_Id(Long userId, Long targetUserId);
}
