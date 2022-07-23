package cleanbook.com.repository.user;

import cleanbook.com.entity.user.filter.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, Long> {
}
