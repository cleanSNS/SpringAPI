package cleanbook.com;

import cleanbook.com.configuration.QuerydslConfig;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.user.User;
import cleanbook.com.domain.user.UserProfile;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

//@DataJpaTest
@Import(QuerydslConfig.class)
@SpringBootTest
@Transactional
public class MyTest {

    @Autowired private PageRepository pageRepository;
    @Autowired private UserRepository userRepository;



    @Test
    void test() {

        User user = new User("aa", "aa", new UserProfile());
        userRepository.save(user);
        pageRepository.save(new Page(1L,user, "제목", "내용"));
        pageRepository.save(new Page(1L,user, "제목", "내용"));
        pageRepository.save(new Page(1L,user, "제목", "내용"));

        for (Page page : pageRepository.findAll()) {
            System.out.println("pageId " + page.getId());
        }
    }
}
