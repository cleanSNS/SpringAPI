package cleanbook.com.repository.page;

import cleanbook.com.configuration.QuerydslConfig;
import cleanbook.com.domain.ResultDto;
import cleanbook.com.domain.page.*;
import cleanbook.com.domain.user.*;
import cleanbook.com.repository.CommentRepository;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.service.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

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
//    @Autowired
//    private EntityManager em;

    private User myUser;
    private Page myPage;

    @BeforeEach
    void init() {

        for (int i = 0; i < 10; i++) {
            UserProfile userProfile = new UserProfile(Integer.toString(i), i, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            userRepository.save(user);

            for (int k = 0; k < 2; k++) {
                Page page = new Page(user, Integer.toString(k), Integer.toString(k));
                createPageImgUrl(page, Integer.toString(k)+"a");
                createPageImgUrl(page, Integer.toString(k)+"b");
                for (int j = 0; j < 10; j++) {
                    new Comment(user, page, "댓글" + j);
                }
                pageRepository.save(page);
                if (i == 0 && k == 0) {
                    myUser = user;
                    myPage = page;
                }
            }
        }
    }

    @Test
    void readPageDtoTest() {

        // when
        Long pageId = myPage.getId();
        PageDto pageDto = pageRepository.readPageDto(pageId);
        System.out.println("pageDto " + pageDto.toString());

        // then
        assertThat(pageDto.getUserDto().getNickname()).isEqualTo(myPage.getUser().getUserProfile().getNickname());
        assertThat(pageDto.getTitle()).isEqualTo(myPage.getTitle());
    }

    @Test
    void readPageImgUrlListTest() {

        // when
        Long pageId = myPage.getId();
        List<String> imgUrlList = pageRepository.readPageImgUrlList(pageId);
        for (String imgUrl : imgUrlList) {
            System.out.println("imgUrl = " + imgUrl);
        }

        // then
        assertThat(imgUrlList.size()).isEqualTo(2);
        assertThat(imgUrlList).isEqualTo(myPage.getImgUrlList().stream().map(imgUrl -> imgUrl.getImgUrl()).collect(Collectors.toList()));

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
        Long pageId = myPage.getId();
        ResultDto<List<CommentDto>> result = pageRepository.readPageCommentList(pageId, PageRequest.of(0,10));
        List<CommentDto> commentDtoList = result.getData();

        // then
        assertThat(commentDtoList.size()).isEqualTo(10);
        assertThat(commentDtoList).extracting("content").contains("댓글0", "댓글5");
    }

    @Test
    @DisplayName("게시글_상세보기")
    void readPageDetailTest() {


        // when
        Long pageId = myPage.getId();
        PageDetailDto pageDetailDto = pageRepository.readPageDetail(pageId);
        UserDto userDto = pageDetailDto.getPageDto().getUserDto();
        List<CommentDto> commentDtoList = pageDetailDto.getCommentDtoList();


        // then
        assertThat(userDto.getNickname()).isEqualTo("0");
        assertThat(pageDetailDto.getPageDto().getTitle()).isEqualTo("0");
        assertThat(commentDtoList.size()).isEqualTo(10);


    }

    @Test
    @DisplayName("팔로우한_유저_게시물_보기")
    void readFollowerPageList() {

        //given
        List<User> userList = userRepository.findAll();
        User user = userList.get(0);
        User user2 = userList.get(1);
        User user3 = userList.get(2);
        User user4 = userList.get(3);
        em.persist(new Follow(user,user2));
        em.persist(new Follow(user,user3));
        em.persist(new Follow(user,user4));


        // when
        ResultDto<List<MainPageDto>> result = pageRepository.readFolloweePageList(user.getId(), null, 3);
        List<MainPageDto> mainPageDtoList = result.getData();
        Long startPageId = result.getStartPageId();
        System.out.println("startPageId = " + startPageId);

        // then
        assertThat(mainPageDtoList.size()).isEqualTo(3);
        assertThat(mainPageDtoList.get(0).getPageDto().getUserDto().getNickname()).isEqualTo(user4.getUserProfile().getNickname());
        assertThat(mainPageDtoList.get(2).getPageDto().getUserDto().getNickname()).isEqualTo(user3.getUserProfile().getNickname());

        // when
        result = pageRepository.readFolloweePageList(user.getId(), startPageId, 3);
        mainPageDtoList = result.getData();

        // then
        assertThat(mainPageDtoList.size()).isEqualTo(3);
        assertThat(mainPageDtoList.get(0).getPageDto().getUserDto().getNickname()).isEqualTo(user3.getUserProfile().getNickname());
        assertThat(mainPageDtoList.get(2).getPageDto().getUserDto().getNickname()).isEqualTo(user2.getUserProfile().getNickname());
    }

    @Test
    @DisplayName("특정_유저_전체_게시글_보기")
    void readUserPageListTest() {

        //given
        UserProfile userProfile = new UserProfile("nickname", 25, GenderType.FEMALE);
        User user = new User("aa", "aa", userProfile);
        userRepository.save(user);
        for (int k = 0; k < 10; k++) {
            Page page = new Page(user, Integer.toString(k), Integer.toString(k));
            for (int i = 0; i < k; i++) {
                createPageImgUrl(page, k +"a");
            }

            pageRepository.save(page);
        }

        // when
        ResultDto<List<UserPageDto>> result = pageRepository.readUserPageList(user.getId(), null, 2);
        List<UserPageDto> userPageDtoList = result.getData();
        Long pageStartIdx = result.getStartPageId();

        // then
        assertThat(userPageDtoList).extracting("title").containsExactly("9","8");

        // when
        result = pageRepository.readUserPageList(user.getId(), pageStartIdx, 3);
        userPageDtoList = result.getData();

        // then
        assertThat(userPageDtoList).extracting("title").containsExactly("7","6","5");
    }

}






