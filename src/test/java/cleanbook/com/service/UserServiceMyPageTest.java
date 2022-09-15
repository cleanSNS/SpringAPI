//package cleanbook.com.service;
//
//import cleanbook.com.entity.enums.GenderType;
//import cleanbook.com.entity.enums.SettingType;
//import cleanbook.com.entity.user.*;
//import cleanbook.com.repository.user.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceMyPageTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks private UserService userService;
//
//    private User user;
//    private Long sequence = 0L;
//    private UserProfile newUserProfile;
//
//    @BeforeEach
//    void init() {
//        UserProfile userProfile = new UserProfile("a",1, GenderType.FEMALE);
//        newUserProfile = new UserProfile("lacram",25, GenderType.MALE);
//        UserSetting userSetting = new UserSetting(new UserNotificationSetting(), new UserFilterSetting());
//        user = new User(1L,"user", "aaa", userProfile, userSetting);
//    }
//
//    @Test
//    @DisplayName("프로필_편집")
//    void changeProfileTest() {
//
//        //given
//        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
//
//
//        // when
//        userService.changeUserProfile(user.getId(), newUserProfile);
//
//
//        // then
//        assertThat(user.getUserProfile().getNickname()).isEqualTo("lacram");
//        assertThat(user.getUserProfile().getAge()).isEqualTo(25);
//        assertThat(user.getUserProfile().getGender()).isEqualTo(GenderType.MALE);
//    }
//
//    @Test
//    @DisplayName("푸쉬알림_설정")
//    void changeNotificationSettingTest() {
//
//        //given
//        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
//        UserNotificationSetting newSetting = new UserNotificationSetting(true, SettingType.FOLLOW_ONLY, SettingType.NONE, true, true);
//
//
//        // when
//        userService.changeUserNotificationSetting(user.getId(), newSetting);
//
//
//        // then
//        assertThat(user.getUserSetting().getUserNotificationSetting().isNotificationFollow()).isEqualTo(true);
//        assertThat(user.getUserSetting().getUserNotificationSetting().getNotificationLike()).isEqualTo(SettingType.NONE);
//
//    }
//
//    @Test
//    @DisplayName("비밀번호_변경")
//    void changePasswordTest() {
//
//        //given
//        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
//        String newPassword = "new";
//
//        // when
//        userService.changePassword(user.getId(), newPassword);
//
//
//        // then
//        assertThat(user.getPassword()).isEqualTo("new");
//
//    }
//
//    @Test
//    @DisplayName("필터링_설정")
//    void changeFilterSettingTest() {
//
//        //given
//        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
//        UserFilterSetting userFilterSetting = new UserFilterSetting(true, true, true);
//
//
//        // when
//        userService.changeUserFilterSetting(user.getId(), userFilterSetting);
//
//
//        // then
//        assertThat(user.getUserSetting().getUserFilterSetting().isFilterAll()).isEqualTo(true);
//
//    }
//}
