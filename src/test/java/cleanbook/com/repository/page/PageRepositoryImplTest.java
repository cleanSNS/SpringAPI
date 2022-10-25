package cleanbook.com.repository.page;

import cleanbook.com.config.QuerydslConfig;
import cleanbook.com.dto.CountDto;
import cleanbook.com.dto.page.*;
import cleanbook.com.dto.user.UserDto;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.enums.SettingType;
import cleanbook.com.entity.page.*;
import cleanbook.com.entity.user.*;
import cleanbook.com.repository.FollowRepository;
import cleanbook.com.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cleanbook.com.entity.page.Page.createPage;
import static cleanbook.com.entity.page.PageImgUrl.createPageImgUrl;
import static cleanbook.com.entity.user.follow.Follow.createFollow;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class PageRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private FollowRepository followRepository;

    private User myUser;
    private Page myPage;

    @BeforeEach
    void init() {

        for (int i = 0; i < 4; i++) {
            User user = userRepository.findById((long) (i + 1)).get();

            for (int k = 0; k < 2; k++) {
                Page page = Page.builder()
                                .user(user)
                                .content(Integer.toString(k))
                                .build();

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
    @DisplayName("게시글 기본 DTO")
    void readPageDtoTest() {

        // when
        Long pageId = myPage.getId();
        PageDto pageDto = pageRepository.readPageDto(1L, pageId);
        System.out.println("pageDto " + pageDto.toString());

        // then
        assertThat(pageDto.getUserDto().getNickname()).isEqualTo(myPage.getUser().getUserProfile().getNickname());
    }

    @Test
    @DisplayName("게시글 이미지 테스트")
    void readPageImgUrlListTest() {

        // when
        Long pageId = myPage.getId();
        List<String> imgUrlList = pageRepository.readPageImgUrlList(pageId);
        for (String imgUrl : imgUrlList) {
            System.out.println("imgUrl = " + imgUrl);
        }

        // then
        assertThat(imgUrlList.size()).isEqualTo(2);
        assertThat(imgUrlList).isEqualTo(myPage.getPageImgUrlList().stream().map(imgUrl -> imgUrl.getImgUrl()).collect(Collectors.toList()));

    }

    @Test
    @DisplayName("게시글 댓글 테스트")
    void readPageCommentListTest() {

        // when
        Long pageId = myPage.getId();
        ResultDto<List<CommentDto>> result = pageRepository.readPageCommentList(null, pageId);
        List<CommentDto> commentDtoList = result.getData();

        // then
        assertThat(commentDtoList.size()).isEqualTo(10);
        assertThat(commentDtoList).extracting("content").contains("댓글0", "댓글5");
    }

    @Test
    @DisplayName("게시글 상세보기")
    void readPageDetailTest() {


        // when
        Long pageId = myPage.getId();
        PageDetailDto pageDetailDto = pageRepository.readPageDetail(null, pageId);
        UserDto userDto = pageDetailDto.getPageDto().getUserDto();
        ResultDto<List<CommentDto>> result = pageRepository.readPageCommentList(null, pageId);
        List<CommentDto> commentDtoList = result.getData();

        // then
        assertThat(userDto.getNickname()).isEqualTo("user1");
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
            User user = User.builder().email("aa").password("aa").userProfile(userProfile).build();
            User user2 = User.builder().email("bb").password("bb").userProfile(userProfile).build();
            User user3 = User.builder().email("cc").password("cc").userProfile(userProfile).build();
            userRepository.save(user);
            userRepository.save(user2);
            userRepository.save(user3);

            PageSetting allSetting = new PageSetting(true, true, SettingType.ALL, true, true);
            PageSetting followOnlySetting = new PageSetting(true, true, SettingType.FOLLOW_ONLY, true, true);
            Page allPage = createPage(user2, PageCreateDto.builder().content("all").pageSetting(allSetting).build());
            Page followOnlyPage = createPage(user3, PageCreateDto.builder().content("follow").pageSetting(followOnlySetting).build());
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
            assertThat(mainPageDtoList.get(0).getPageDto().getPageId()).isEqualTo(allPage.getId());
        }

        @Test
        @DisplayName("맞팔일시 FOLLOW_ONLY 게시글 조회 가능 ")
        void canReadFollowOnly() {

            //given
            UserProfile userProfile = new UserProfile("aa", 5, GenderType.FEMALE);
            User user = User.builder().email("aa").password("aa").userProfile(userProfile).build();
            User user2 = User.builder().email("bb").password("bb").userProfile(userProfile).build();
            User user3 = User.builder().email("cc").password("cc").userProfile(userProfile).build();
            userRepository.save(user);
            userRepository.save(user2);
            userRepository.save(user3);

            PageSetting allSetting = new PageSetting(true, true, SettingType.ALL, true, true);
            PageSetting followOnlySetting = new PageSetting(true, true, SettingType.FOLLOW_ONLY, true, true);
            Page allPage = createPage(user2, PageCreateDto.builder().content("all").pageSetting(allSetting).build());
            Page followOnlyPage = createPage(user3, PageCreateDto.builder().content("follow").pageSetting(followOnlySetting).build());
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
            User user = User.builder().email("aa").password("aa").userProfile(userProfile).build();
            User user2 = User.builder().email("bb").password("bb").userProfile(userProfile).build();
            userRepository.save(user);
            userRepository.save(user2);

            PageSetting noneSetting = new PageSetting(true, true, SettingType.NONE, true, true);
            Page nonePage = createPage(user2, PageCreateDto.builder().content("none").pageSetting(noneSetting).build());
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
            User user = User.builder().email("aa").password("aa").userProfile(userProfile).build();
            User user2 = User.builder().email("bb").password("bb").userProfile(userProfile).build();
            userRepository.save(user);
            userRepository.save(user2);

            PageSetting allSetting = new PageSetting(true, true, SettingType.ALL, true, true);
            PageSetting followSetting = new PageSetting(true, true, SettingType.FOLLOW_ONLY, true, true);
            PageSetting noneSetting = new PageSetting(true, true, SettingType.NONE, true, true);
            pageRepository.save(createPage(user2, PageCreateDto.builder().content("all").imgUrlList(List.of("url")).pageSetting(allSetting).build()));
            pageRepository.save(createPage(user2, PageCreateDto.builder().content("follow").imgUrlList(List.of("url")).pageSetting(followSetting).build()));
            pageRepository.save(createPage(user2, PageCreateDto.builder().content("none").imgUrlList(List.of("url")).pageSetting(noneSetting).build()));

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
            User user = User.builder().email("aa").password("aa").userProfile(userProfile).build();
            User user2 = User.builder().email("bb").password("bb").userProfile(userProfile).build();
            userRepository.save(user);
            userRepository.save(user2);

            PageSetting allSetting = new PageSetting(true, true, SettingType.ALL, true, true);
            PageSetting followSetting = new PageSetting(true, true, SettingType.FOLLOW_ONLY, true, true);
            PageSetting noneSetting = new PageSetting(true, true, SettingType.NONE, true, true);
            pageRepository.save(createPage(user2, PageCreateDto.builder().content("all").imgUrlList(List.of("url")).pageSetting(allSetting).build()));
            pageRepository.save(createPage(user2, PageCreateDto.builder().content("follow").imgUrlList(List.of("url")).pageSetting(followSetting).build()));
            pageRepository.save(createPage(user2, PageCreateDto.builder().content("none").imgUrlList(List.of("url")).pageSetting(noneSetting).build()));

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
            User user = User.builder().email("aa").password("aa").userProfile(userProfile).build();
            User user2 = User.builder().email("bb").password("bb").userProfile(userProfile).build();
            userRepository.save(user);
            userRepository.save(user2);

            PageSetting allSetting = new PageSetting(true, true, SettingType.ALL, true, true);
            PageSetting followSetting = new PageSetting(true, true, SettingType.FOLLOW_ONLY, true, true);
            PageSetting noneSetting = new PageSetting(true, true, SettingType.NONE, true, true);
            pageRepository.save(createPage(user2, PageCreateDto.builder().content("all").imgUrlList(List.of("url")).pageSetting(allSetting).build()));
            pageRepository.save(createPage(user2, PageCreateDto.builder().content("follow").imgUrlList(List.of("url")).pageSetting(followSetting).build()));
            pageRepository.save(createPage(user2, PageCreateDto.builder().content("none").imgUrlList(List.of("url")).pageSetting(noneSetting).build()));



            // when
            ResultDto<List<UserPageDto>> result = pageRepository.readUserPageList(user2.getId(), user2.getId(), null, 5);
            List<UserPageDto> userPageDtoList = result.getData();


            // then
            assertThat(userPageDtoList.size()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("해시태그")
    class hashtag{

        @Test
        @DisplayName("해시태그 검색")
        void readPageByHashtag() {

            //given
            PageCreateDto pageCreateDto = PageCreateDto.builder()
                    .content("내용")
                    .imgUrlList(List.of("url"))
                    .pageHashtagList(Arrays.asList("바다", "수영"))
                    .build();

            PageCreateDto pageCreateDto2 = PageCreateDto.builder()
                    .content("내용2")
                    .imgUrlList(List.of("url"))
                    .pageHashtagList(Arrays.asList("바다1", "수영"))
                    .build();

            PageCreateDto pageCreateDto3 = PageCreateDto.builder()
                    .content("내용3")
                    .imgUrlList(List.of("url"))
                    .pageHashtagList(Arrays.asList("수염", "수영"))
                    .build();

            pageRepository.save(createPage(myUser, pageCreateDto));
            pageRepository.save(createPage(myUser, pageCreateDto2));
            pageRepository.save(createPage(myUser, pageCreateDto3));


            // when
            ResultDto<List<UserPageDto>> resultDto = pageRepository.readPageByHashtag("바다", null, 10);
            ResultDto<List<UserPageDto>> resultDto2 = pageRepository.readPageByHashtag("수영", null, 10);
            List<UserPageDto> userPageDtoList = resultDto.getData();
            List<UserPageDto> userPageDtoList2 = resultDto2.getData();
            Long startId = resultDto.getStartId();


            // then
            assertThat(userPageDtoList.size()).isEqualTo(1);
            assertThat(userPageDtoList2.size()).isEqualTo(3);

            // 최신순으로
            assertThat(userPageDtoList2.get(0).getPageId()).isGreaterThan(userPageDtoList2.get(2).getPageId());

            resultDto = pageRepository.readPageByHashtag("바다", startId, 10);

            assertThat(resultDto.getData().size()).isEqualTo(0);
        }

        @Test
        @DisplayName("해시태그 검색")
        void getPageListCountByHashtag() {



            //given
            PageCreateDto pageCreateDto = PageCreateDto.builder()
                    .content("내용")
                    .pageHashtagList(Arrays.asList("바다", "수영"))
                    .build();

            PageCreateDto pageCreateDto2 = PageCreateDto.builder()
                    .content("내용2")
                    .pageHashtagList(Arrays.asList("바다1", "수영"))
                    .build();

            PageCreateDto pageCreateDto3 = PageCreateDto.builder()
                    .content("내용3")
                    .pageHashtagList(Arrays.asList("수염", "수영"))
                    .build();

            pageRepository.save(createPage(myUser, pageCreateDto));
            pageRepository.save(createPage(myUser, pageCreateDto2));
            pageRepository.save(createPage(myUser, pageCreateDto3));


            ResultDto<CountDto> resultDto = pageRepository.getPageListCountByHashtag("수영");
            ResultDto<CountDto> resultDto2 = pageRepository.getPageListCountByHashtag("바다");
            CountDto countDto = resultDto.getData();
            CountDto countDto2 = resultDto2.getData();

            assertThat(countDto.getCount()).isEqualTo(3);
            assertThat(countDto2.getCount()).isEqualTo(1);
        }
    }


}






