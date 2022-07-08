package cleanbook.com.repository.page;

import cleanbook.com.configuration.QuerydslConfig;
import cleanbook.com.domain.page.*;
import cleanbook.com.domain.user.GenderType;
import cleanbook.com.domain.user.User;
import cleanbook.com.domain.user.UserProfile;
import cleanbook.com.domain.user.UserDto;
import cleanbook.com.repository.CommentRepository;
import cleanbook.com.repository.user.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import javax.persistence.PersistenceContext;
import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

import static cleanbook.com.domain.page.PageImgUrl.createPageImgUrl;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
//@SpringBootTest
//@Transactional
//@Rollback(value = false)
class PageRepositoryImplTest {

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

    @BeforeEach
    void init() {

        for (int i = 1; i < 11; i++) {
            UserProfile userProfile = new UserProfile(Integer.toString(i), i, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            userRepository.save(user);

            Page page = new Page(user, Integer.toString(i), Integer.toString(i));
            createPageImgUrl(page, "aaa");
            createPageImgUrl(page, "bbb");

            for (int j = 0; j < 10; j++) {
                new Comment(user, page, "댓글" + j);
            }

            pageRepository.save(page);
        }

    }

    @Test
    void readPageDtoTest() {

        // when
        Long pageId = pageRepository.findAll().get(0).getId();
        PageDto pageDto = pageRepository.readPageDto(pageId);
        System.out.println("pageDto " + pageDto.toString());

        // then
        assertThat(pageDto.getUserDto().getNickname()).isEqualTo("1");
        assertThat(pageDto.getTitle()).isEqualTo("1");
    }

    @Test
    void readPageImgUrlListTest() {

        // when
        Long pageId = pageRepository.findAll().get(0).getId();
        List<String> imgUrlList = pageRepository.readPageImgUrlList(pageId);
        for (String imgUrl : imgUrlList) {
            System.out.println("imgUrl = " + imgUrl);
        }

        // then
        assertThat(imgUrlList.size()).isEqualTo(2);
        assertThat(imgUrlList).containsExactly("aaa", "bbb");

    }

    @Test
    void readPageImgUrlListEmptyTest() {

        // given
        User user = new User("a", "a", new UserProfile());
        userRepository.save(user);
        Long pageId = pageRepository.save(new Page(user, "제목", "내용")).getId();


        // when
        List<String> imgUrlList = pageRepository.readPageImgUrlList(pageId);
        for (String imgUrl : imgUrlList) {
            System.out.println("imgUrl = " + imgUrl);
        }

        // then
        assertThat(imgUrlList.size()).isEqualTo(0);

    }
    
    @Test
    void readPageCommentListTest() {
    
        // when
        Long pageId = pageRepository.findAll().get(0).getId();
        List<CommentDto> commentDtoList = pageRepository.readPageCommentList(pageId);
        for (CommentDto commentDto : commentDtoList) {
            System.out.println("commentDto = " + commentDto);
        }

        // then
        assertThat(commentDtoList.size()).isEqualTo(10);
        assertThat(commentDtoList).extracting("content").contains("댓글0", "댓글5");
    }

    @Test
    @DisplayName("게시글_상세보기")
    void readPageDetailTest() {


        // when
        Long pageId = pageRepository.findAll().get(0).getId();
        PageDetailDto pageDetailDto = pageRepository.readPageDetail(pageId);
        UserDto userDto = pageDetailDto.getPageDto().getUserDto();
        List<CommentDto> commentDtoList = pageDetailDto.getCommentDtoList();


        // then
        assertThat(userDto.getNickname()).isEqualTo("1");
        assertThat(pageDetailDto.getPageDto().getTitle()).isEqualTo("1");
        assertThat(commentDtoList.size()).isEqualTo(10);


    }
}






