package cleanbook.com.service;

import cleanbook.com.configuration.QuerydslConfig;
import cleanbook.com.domain.page.Comment;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.page.PageCreateDto;
import cleanbook.com.domain.page.PageUpdateDto;
import cleanbook.com.domain.user.GenderType;
import cleanbook.com.domain.user.User;
import cleanbook.com.domain.user.UserProfile;
import cleanbook.com.exception.NoAuthroizationException;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;


import java.util.Optional;

import static cleanbook.com.domain.page.PageImgUrl.createPageImgUrl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class PageServiceTest {

    @Spy
    private UserRepository userRepository;
    @Spy
    private PageRepository pageRepository;
    @InjectMocks
    private PageService pageService;

    @Test
    @DisplayName("게시글_저장")
    void pageSaveTest() {

        //given
        UserProfile userProfile = new UserProfile("a",1, GenderType.FEMALE);
        User user = new User(1L,"user", "aaa", userProfile);

        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
        given(pageRepository.save(any(Page.class))).willReturn(new Page("bb", "bb"));

        User findUser = userRepository.findById(1L).get();

        // when
        Page findPage = pageService.createPage(findUser.getId(), new PageCreateDto("bb", "bb"));
        findUser = userRepository.findById(1L).get();

        // then
        assertThat(findPage.getTitle()).isEqualTo("bb");
        assertThat(findUser.getPageList().size()).isEqualTo(1);
    }

    @Nested
    @DisplayName("게시글_수정")
    class updatePageDto {
        
        @Test
        @DisplayName("권한O")
        void haveAuth() {
        
            //given
            User user = new User(1L, "email", "password", null);
            Page oldPage = new Page(1L, user, "old", "old");
            userRepository.save(user);

            given(pageRepository.findById(any(Long.class))).willReturn(Optional.of(oldPage));

            // when
            Page newPage = pageService.updatePage(user.getId(), oldPage.getId(), new PageUpdateDto("new", "new"));


            // then
            assertThat(newPage.getTitle()).isEqualTo("new");
            
        }

        @Test
        @DisplayName("권한X")
        void noAuth() {

            //given
            User user = new User(1L, "email", "password", null);
            Page oldPage = new Page(1L, user, "old", "old");
            userRepository.save(user);

            given(pageRepository.findById(any(Long.class))).willReturn(Optional.of(oldPage));

            // when
            // then
            assertThrows(NoAuthroizationException.class, () -> {
                pageService.updatePage(2L, oldPage.getId(), new PageUpdateDto("new", "new"));
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
            User user = new User(1L, "email", "password", null);
            Page oldPage = new Page(1L, user, "old", "old");
            userRepository.save(user);

            given(pageRepository.findById(any(Long.class))).willReturn(Optional.of(oldPage));
            given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));

            // when
            Long pageId = pageService.deletePage(user.getId(), oldPage.getId());


            // then
            assertThat(user.getPageList().size()).isEqualTo(0);
        }

        @Test
        @DisplayName("권한X")
        void noAuth() {

            //given
            User user = new User(1L, "email", "password", null);
            Page oldPage = new Page(1L, user, "old", "old");
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