package cleanbook.com.repository.user;

import cleanbook.com.domain.user.like.LikePage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePageRepository extends JpaRepository<LikePage, Long> {
}
