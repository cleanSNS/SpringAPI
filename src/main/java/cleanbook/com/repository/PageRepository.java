package cleanbook.com.repository;

import cleanbook.com.domain.page.Comment;
import cleanbook.com.domain.page.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long> {
}
