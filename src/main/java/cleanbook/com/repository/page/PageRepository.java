package cleanbook.com.repository.page;

import cleanbook.com.domain.page.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long>, PageRepositoryCustom {

}
