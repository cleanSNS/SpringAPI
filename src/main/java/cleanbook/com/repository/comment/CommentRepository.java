package cleanbook.com.repository.comment;

import cleanbook.com.entity.page.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Optional<Comment> findFirstByGroupOrderByOrderDesc(int group);
    Optional<List<Comment>> findCommentsByGroup(int group);
}
