package cleanbook.com.repository.user;

import cleanbook.com.domain.user.like.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
}
