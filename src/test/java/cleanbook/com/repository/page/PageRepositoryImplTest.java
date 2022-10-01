package cleanbook.com.repository.page;

import cleanbook.com.config.QuerydslConfig;
import cleanbook.com.dto.page.*;
import cleanbook.com.dto.user.UserDto;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.enums.SettingType;
import cleanbook.com.entity.page.*;
import cleanbook.com.entity.user.*;
import cleanbook.com.entity.user.follow.Follow;
import cleanbook.com.exception.exceptions.NoMorePageException;
import cleanbook.com.repository.FollowRepository;
import cleanbook.com.repository.comment.CommentRepository;
import cleanbook.com.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

import static cleanbook.com.entity.page.Page.createPage;
import static cleanbook.com.entity.page.PageImgUrl.createPageImgUrl;
import static cleanbook.com.entity.user.follow.Follow.createFollow;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(QuerydslConfig.class)
//@SpringBootTest
//@Transactional
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
    private FollowRepository followRepository;
    @Autowired
    private TestEntityManager em;
//    @Autowired
//    private EntityManager em;

    private User myUser;
    private Page myPage;

    @BeforeEach
    void init() {

        for (int i = 0; i < 4; i++) {
            User user = userRepository.findById((long) (i + 1)).get();

            for (int k = 0; k < 2; k++) {
                Page page = new Page(user, Integer.toString(k));
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
        Long pageId = pageRepository.save(new Page(user,  "내용")).getId();


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
        ResultDto<List<CommentDto>> result = pageRepository.readPageCommentList(pageId);
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
        PageDetailDto pageDetailDto = pageRepository.readPageDetail(null, pageId);
        UserDto userDto = pageDetailDto.getPageDto().getUserDto();
        ResultDto<List<CommentDto>> result = pageRepository.readPageCommentList(pageId);
        List<CommentDto> commentDtoList = result.getData();

        // then
        assertThat(userDto.getNickname()).isEqualTo("name");
        assertThat(commentDtoList.size()).isEqualTo(10);


    }

    @Nested
    @DisplayName("메인페이지 게시물 보기")
    class mainPage{

        @Test
        @DisplayName("맞팔 안될시 FOLLOW_ONLY 게시글 조회 불가")
        void cantReadFollowOnly() {

            //given
            UserProfile userProfile = new UserProfile("aa", 5, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            User user2 = new User("bb", "bb", userProfile);
            User user3 = new User("cc", "cc", userProfile);
            userRepository.save(user);
            userRepository.save(user2);
            userRepository.save(user3);

            PageSetting allSetting = new PageSetting(true, true, SettingType.ALL, true, true);
            PageSetting followOnlySetting = new PageSetting(true, true, SettingType.FOLLOW_ONLY, true, true);
            Page allPage = createPage(user2, new PageCreateDto("all", allSetting));
            Page followOnlyPage = createPage(user3, new PageCreateDto("follow", followOnlySetting));
            pageRepository.save(allPage);
            pageRepository.save(followOnlyPage);

            followRepository.save(createFollow(user,user2));
            followRepository.save(createFollow(user,user3));
            followRepository.save(createFollow(user2,user));


            // when
            ResultDto<List<MainPageDto>> result = pageRepository.readFolloweePageList(user.getId(), null, 3);
            List<MainPageDto> mainPageDtoList = result.getData();
            Long startPageId = result.getStartId();

            // then
            assertThat(mainPageDtoList.size()).isEqualTo(1);
            assertThat(mainPageDtoList.get(0).getPageDto().getContent()).isEqualTo("all");
        }

        @Test
        @DisplayName("맞팔일시 FOLLOW_ONLY 게시글 조회 가능 ")
        void canReadFollowOnly() {

            //given
            UserProfile userProfile = new UserProfile("aa", 5, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            User user2 = new User("bb", "bb", userProfile);
            User user3 = new User("cc", "cc", userProfile);
            userRepository.save(user);
            userRepository.save(user2);
            userRepository.save(user3);

            PageSetting allSetting = new PageSetting(true, true, SettingType.ALL, true, true);
            PageSetting followOnlySetting = new PageSetting(true, true, SettingType.FOLLOW_ONLY, true, true);
            Page allPage = createPage(user2, new PageCreateDto("all", allSetting));
            Page followOnlyPage = createPage(user3, new PageCreateDto("follow", followOnlySetting));
            pageRepository.save(allPage);
            pageRepository.save(followOnlyPage);

            followRepository.save(createFollow(user,user2));
            followRepository.save(createFollow(user,user3));
            followRepository.save(createFollow(user2,user));
            followRepository.save(createFollow(user3,user));

            // when
            ResultDto<List<MainPageDto>> result = pageRepository.readFolloweePageList(user.getId(), null, 3);
            List<MainPageDto> mainPageDtoList = result.getData();
            Long startPageId = result.getStartId();

            // then
            assertThat(mainPageDtoList.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("맞팔이어도 none게시글은 조회 불가")
        void cantReadNone() {

            //given
            UserProfile userProfile = new UserProfile("aa", 5, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            User user2 = new User("bb", "bb", userProfile);
            userRepository.save(user);
            userRepository.save(user2);

            PageSetting noneSetting = new PageSetting(true, true, SettingType.NONE, true, true);
            Page nonePage = createPage(user2, new PageCreateDto("none", noneSetting));
            pageRepository.save(nonePage);

            followRepository.save(createFollow(user,user2));
            followRepository.save(createFollow(user2,user));

            // when
            ResultDto<List<MainPageDto>> result = pageRepository.readFolloweePageList(user.getId(), null, 3);
            List<MainPageDto> mainPageDtoList = result.getData();
            Long startPageId = result.getStartId();

            // then
            assertThat(mainPageDtoList.size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("특정 유저 전체 게시글 보기")
    class readUserPageList{

        @Test
        @DisplayName("ALL")
        void allTest() {

            //given
            UserProfile userProfile = new UserProfile("aa", 5, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            User user2 = new User("bb", "bb", userProfile);
            userRepository.save(user);
            userRepository.save(user2);

            PageSetting allSetting = new PageSetting(true, true, SettingType.ALL, true, true);
            PageSetting followSetting = new PageSetting(true, true, SettingType.FOLLOW_ONLY, true, true);
            PageSetting noneSetting = new PageSetting(true, true, SettingType.NONE, true, true);
            pageRepository.save(createPage(user2, new PageCreateDto("all", allSetting)));
            pageRepository.save(createPage(user2, new PageCreateDto("follow", followSetting)));
            pageRepository.save(createPage(user2, new PageCreateDto("none", noneSetting)));

            followRepository.save(createFollow(user,user2));

            // when
            ResultDto<List<UserPageDto>> result = pageRepository.readUserPageList(user.getId(), user2.getId(), null, 5);
            List<UserPageDto> userPageDtoList = result.getData();

            // then
            assertThat(userPageDtoList.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("followOnly")
        void followOnlyTest() {

            //given
            UserProfile userProfile = new UserProfile("aa", 5, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            User user2 = new User("bb", "bb", userProfile);
            userRepository.save(user);
            userRepository.save(user2);

            PageSetting allSetting = new PageSetting(true, true, SettingType.ALL, true, true);
            PageSetting followSetting = new PageSetting(true, true, SettingType.FOLLOW_ONLY, true, true);
            PageSetting noneSetting = new PageSetting(true, true, SettingType.NONE, true, true);
            pageRepository.save(createPage(user2, new PageCreateDto("all", allSetting)));
            pageRepository.save(createPage(user2, new PageCreateDto("follow", followSetting)));
            pageRepository.save(createPage(user2, new PageCreateDto("none", noneSetting)));

            followRepository.save(createFollow(user,user2));
            followRepository.save(createFollow(user2,user));


            // when
            ResultDto<List<UserPageDto>> result = pageRepository.readUserPageList(user.getId(), user2.getId(), null, 5);
            List<UserPageDto> userPageDtoList = result.getData();


            // then
            assertThat(userPageDtoList.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("none은 자신만 볼 수 있음")
        void noneTest() {

            //given
            UserProfile userProfile = new UserProfile("aa", 5, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            User user2 = new User("bb", "bb", userProfile);
            userRepository.save(user);
            userRepository.save(user2);

            PageSetting allSetting = new PageSetting(true, true, SettingType.ALL, true, true);
            PageSetting followSetting = new PageSetting(true, true, SettingType.FOLLOW_ONLY, true, true);
            PageSetting noneSetting = new PageSetting(true, true, SettingType.NONE, true, true);
            pageRepository.save(createPage(user2, new PageCreateDto("all", allSetting)));
            pageRepository.save(createPage(user2, new PageCreateDto("follow", followSetting)));
            pageRepository.save(createPage(user2, new PageCreateDto("none", noneSetting)));



            // when
            ResultDto<List<UserPageDto>> result = pageRepository.readUserPageList(user2.getId(), user2.getId(), null, 5);
            List<UserPageDto> userPageDtoList = result.getData();


            // then
            assertThat(userPageDtoList.size()).isEqualTo(3);
        }
    }



}






