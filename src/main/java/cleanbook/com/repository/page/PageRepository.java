package cleanbook.com.repository.page;

import cleanbook.com.entity.page.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long>, PageRepositoryCustom {

}
