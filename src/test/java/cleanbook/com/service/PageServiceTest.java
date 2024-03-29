package cleanbook.com.service;

import cleanbook.com.entity.page.Page;
import cleanbook.com.dto.page.PageCreateDto;
import cleanbook.com.dto.page.PageUpdateDto;
import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.page.PageSetting;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.UserProfile;
import cleanbook.com.exception.exceptions.NoAuthroizationException;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.util.AIUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;


@ExtendWith(MockitoExtension.class)
class PageServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PageRepository pageRepository;
    @Mock
    private AwsS3Service awsS3Service;
    @InjectMocks
    private PageService pageService;

    @Test
    @DisplayName("게시글_저장")
    void pageSaveTest() {

        //given
        User user = User.builder().id(1L).build();

        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
        given(pageRepository.save(any(Page.class))).willReturn(new Page("bb"));

        User findUser = userRepository.findById(user.getId()).get();


        try (MockedStatic<AIUtils> utilities = Mockito.mockStatic(AIUtils.class)){
            utilities.when(() -> AIUtils.filterContent(any(String.class))).thenReturn("filteredContent");

            // when
            Page findPage = pageService.createPage(findUser.getId(), PageCreateDto.builder().build());
            findUser = userRepository.findById(1L).get();

            // then
            assertThat(findUser.getPageList().size()).isEqualTo(1);
        }

    }

    @Nested
    @DisplayName("게시글_수정")
    class updatePageDto {

        @Test
        @DisplayName("권한O")
        void haveAuth() {

            //given
            User user = new User(1L, "email", "password", null);
            Page oldPage = Page.builder().id(1L).user(user).content("old").build();
            userRepository.save(user);

            given(pageRepository.findById(any(Long.class))).willReturn(Optional.of(oldPage));

            // when
            pageService.updatePage(user.getId(), oldPage.getId(), new PageUpdateDto("new"));


            // then
        }

        @Test
        @DisplayName("권한X")
        void noAuth() {

            //given
            User user = User.builder().id(1L).build();
            Page oldPage = new Page(1L, user, "old");
            userRepository.save(user);

            given(pageRepository.findById(any(Long.class))).willReturn(Optional.of(oldPage));

            // when
            // then
            assertThrows(NoAuthroizationException.class, () -> {
                pageService.updatePage(2L, oldPage.getId(), new PageUpdateDto("new"));
            });
        }
    }

    @Nested
    @DisplayName("게시글_삭제")
    class deletePageDto {

        @Test
        @DisplayName("권한O")
        void haveAuth() {

            //given
            User user = User.builder().id(1L).build();
            Page oldPage = new Page(1L, user, "old");
            userRepository.save(user);

            given(pageRepository.findById(any(Long.class))).willReturn(Optional.of(oldPage));
            given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));

            // when
            pageService.deletePage(user.getId(), oldPage.getId());


            // then
            assertThat(user.getPageList().size()).isEqualTo(0);
        }

        @Test
        @DisplayName("권한X")
        void noAuth() {

            //given
            User user = User.builder().id(1L).build();
            Page oldPage = new Page(1L, user, "old");
            userRepository.save(user);

            given(pageRepository.findById(any(Long.class))).willReturn(Optional.of(oldPage));
            given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));

            // when
            // then
            assertThrows(NoAuthroizationException.class, () -> {
                pageService.deletePage(2L, oldPage.getId());
            });
            assertThat(user.getPageList().size()).isEqualTo(1);
        }
    }
}