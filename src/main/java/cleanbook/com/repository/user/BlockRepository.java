package cleanbook.com.repository.user;

import cleanbook.com.domain.user.block.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, Long> {
}
