package cleanbook.com.repository.user;

import cleanbook.com.entity.user.block.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findByUser_IdAndTargetUser_Id(Long userId, Long targetUserId);
}
