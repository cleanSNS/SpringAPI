package cleanbook.com;

import cleanbook.com.configuration.QuerydslConfig;
import cleanbook.com.domain.page.Comment;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.page.PageCreateDto;
import cleanbook.com.domain.user.Follow;
import cleanbook.com.domain.user.GenderType;
import cleanbook.com.domain.user.User;
import cleanbook.com.domain.user.UserProfile;
import cleanbook.com.repository.CommentRepository;
import cleanbook.com.repository.page.PageImgUrlRepository;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.service.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static cleanbook.com.domain.page.PageImgUrl.createPageImgUrl;

@DataJpaTest
@Import(QuerydslConfig.class)
//@SpringBootTest
//@Transactional
//@Rollback(value = false)
class MyTest {

    @Autowired
    private PageService pageService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PageImgUrlRepository pageImgUrlRepository;
    @Autowired
    private TestEntityManager em;
//    @Autowired
//    private EntityManager em;

    @BeforeEach
    void init() {

        for (int i = 0; i < 10; i++) {
            UserProfile userProfile = new UserProfile(Integer.toString(i), i, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            userRepository.save(user);

            for (int k = 0; k < 2; k++) {
                Page page = new Page(user, Integer.toString(k), Integer.toString(k));
                createPageImgUrl(page, Integer.toString(k) + "a");
                createPageImgUrl(page, Integer.toString(k) + "b");
                for (int j = 0; j < 10; j++) {
                    new Comment(user, page, "댓글" + j);
                }
                pageRepository.save(page);
            }
        }
    }

    @Test
    void aaa() {


        //given
        User user = userRepository.findById(1L).get();
        System.out.println("size " + user.getPageList().size());

        // when
        System.out.println("user.getId() = " + user.getId());
        pageService.createPage(user.getId(), new PageCreateDto("제목", "내용"));


        // then
        System.out.println("size " + user.getPageList().size());


    }
}
