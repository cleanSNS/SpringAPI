package cleanbook.com.repository.comment;

import cleanbook.com.entity.page.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Optional<Comment> findFirstByGroupOrderByOrderAsc(int group);
    Optional<Comment> findFirstByGroupOrderByOrderDesc(int group);
    Optional<Comment> findFirstByPage_IdAndGroupOrderByOrderDesc(Long pageId, int group);
    Optional<Comment> findFirstByPage_IdAndGroupOrderByOrderAsc(Long pageId, int group);
    Optional<Comment> findFirstByPage_IdOrderByGroupDesc(Long pageId);
    int getNestCommentCount(Long pageId, int group);
}
