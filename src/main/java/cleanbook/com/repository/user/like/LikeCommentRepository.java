package cleanbook.com.repository.user.like;

import cleanbook.com.entity.user.like.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    Optional<LikeComment> findByComment_IdAndUser_Id(Long commentId, Long userId);
}
