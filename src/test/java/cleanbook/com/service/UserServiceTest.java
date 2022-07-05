package cleanbook.com.service;

import cleanbook.com.domain.user.GenderType;
import cleanbook.com.domain.user.User;
import cleanbook.com.domain.user.UserProfile;
import cleanbook.com.repository.FollowRepository;
import cleanbook.com.repository.user.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void saveTest() {
        //given
        UserProfile userProfile = new UserProfile("a",1, GenderType.FEMALE);
        User user = new User(1L,"aaa", "aaa", userProfile);
//        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));

        // when
        userRepository.save(user);

        // then
        assertThat(userRepository.findById(user.getId()).get()).isEqualTo(user);
    }

    @Test
    void followUserTest() {
        //given
        UserProfile userProfile = new UserProfile("a",1, GenderType.FEMALE);
        User user = new User(2L,"user", "aaa", userProfile);
        User targetUser = new User(1L,"target", "aaa", userProfile);

//        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));

        // when
        userRepository.save(user);
        userRepository.save(targetUser);
        userService.followUser(user.getId(), targetUser.getId());

        // then
        assertThat(user.getFolloweeList().size()).isEqualTo(1);
        assertThat(user.getFolloweeList().get(0).getUser()).isEqualTo(user);
        assertThat(targetUser.getFollowerList().get(0).getTargetUser()).isEqualTo(targetUser);
    }
}