package cleanbook.com.service;

import cleanbook.com.domain.page.Comment;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.user.GenderType;
import cleanbook.com.domain.user.User;
import cleanbook.com.domain.user.UserProfile;
import cleanbook.com.domain.user.filter.Filter;
import cleanbook.com.domain.user.UserDto;
import cleanbook.com.repository.user.FilterRepository;
import cleanbook.com.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceFilterTest {

    @Mock private UserRepository userRepository;
    @Mock private FilterRepository filterRepository;

    @InjectMocks private UserService userService;

    private User user;
    private User user3;
    private User targetUser;
    private Page page;
    private Comment comment;
    private Long sequence = 0L;

    @BeforeEach
    void init() {
        UserProfile userProfile = new UserProfile("a",1, GenderType.FEMALE);
        UserProfile userProfile2 = new UserProfile("b",1, GenderType.FEMALE);
        UserProfile userProfile3 = new UserProfile("c",1, GenderType.FEMALE);
        user = new User(1L,"user", "aaa", userProfile);
        targetUser = new User(2L,"targetUser", "aaa", userProfile2);
        user3 = new User(3L,"user3", "aaa", userProfile3);
        page = new Page(1L, user, "제목", "내용");
        comment = new Comment(1L, user, page, "내용");
    }

    @Test
    @DisplayName("필터링할_유저_추가")
    void filterUser() {

        //given
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
        given(filterRepository.save(any(Filter.class))).willReturn(new Filter(1L, user, targetUser));

        // when
        Long filterId = userService.filterUser(user.getId(), targetUser.getId());


        // then
        assertThat(user.getNotFilterUserList().size()).isEqualTo(1);
        assertThat(user.getNotFilterUserList().get(0).getTargetUser()).isEqualTo(targetUser);

    }

    @Test
    @DisplayName("필터링한_유저_전체조회")
    void readFilteredUserList() {

        //given
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
        given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
        given(filterRepository.save(any(Filter.class))).willReturn(new Filter(++sequence, user, targetUser));

        // when
        userService.filterUser(user.getId(), targetUser.getId());
        userService.filterUser(user.getId(), user3.getId());
        List<UserDto> userDtoList = userService.readFilteredUserList(user.getId());


        // then
        assertThat(userDtoList.size()).isEqualTo(2);
        assertThat(userDtoList.get(0).getUserId()).isEqualTo(2L);
        assertThat(userDtoList.get(1).getUserId()).isEqualTo(3L);
        assertThat(userDtoList.get(0).getNickname()).isEqualTo("b");
        assertThat(userDtoList.get(1).getNickname()).isEqualTo("c");
    }

    @Test
    @DisplayName("필터링한_유저_삭제")
    void unfilterUserTest() {

        //given
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
        given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
        given(filterRepository.save(any(Filter.class))).willReturn(new Filter(++sequence, user, targetUser));

        // when
        userService.filterUser(user.getId(), targetUser.getId());
        userService.filterUser(user.getId(), user3.getId());
        List<UserDto> userDtoList = userService.readFilteredUserList(user.getId());


        // then
        assertThat(userDtoList.size()).isEqualTo(2);
        assertThat(userDtoList.get(0).getUserId()).isEqualTo(2L);
        assertThat(userDtoList.get(1).getUserId()).isEqualTo(3L);
        assertThat(userDtoList.get(0).getNickname()).isEqualTo("b");
        assertThat(userDtoList.get(1).getNickname()).isEqualTo("c");

        // when
        userService.unfilterUser(user.getId(), targetUser.getId());
        userDtoList = userService.readFilteredUserList(user.getId());

        // then
        assertThat(userDtoList.size()).isEqualTo(1);
        assertThat(userDtoList.get(0).getUserId()).isEqualTo(3L);
        assertThat(userDtoList.get(0).getNickname()).isEqualTo("c");

    }
}
