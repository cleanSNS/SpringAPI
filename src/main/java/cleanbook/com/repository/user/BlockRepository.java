package cleanbook.com.repository.user;

import cleanbook.com.entity.user.block.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, Long> {
}
