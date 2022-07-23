package cleanbook.com.repository.user.like;

import cleanbook.com.entity.user.like.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
}
