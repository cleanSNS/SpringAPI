package cleanbook.com.repository.user.like;

import cleanbook.com.entity.user.like.LikePage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikePageRepository extends JpaRepository<LikePage, Long> {
    Optional<LikePage> findByPage_IdAndUser_Id(Long pageId, Long userId);
}
