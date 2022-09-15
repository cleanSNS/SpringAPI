//package cleanbook.com.service;
//
//import cleanbook.com.config.QuerydslConfig;
//import cleanbook.com.dto.user.UserSignUpDto;
//import cleanbook.com.entity.enums.GenderType;
//import cleanbook.com.entity.page.Comment;
//import cleanbook.com.entity.page.Page;
//import cleanbook.com.entity.user.User;
//import cleanbook.com.entity.user.UserActive;
//import cleanbook.com.entity.user.UserProfile;
//import cleanbook.com.exception.exceptions.UserDuplicateException;
//import cleanbook.com.repository.UserActiveRepository;
//import cleanbook.com.repository.user.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@Import(QuerydslConfig.class)
//@ExtendWith(MockitoExtension.class)
//class UserAuthServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private UserActiveRepository userActiveRepository;
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private UserAuthService userAuthService;
//
//    private User user;
//    private User user3;
//    private User targetUser;
//    private Page page;
//    private Comment comment;
//    private Long sequence = 0L;
//
//    @BeforeEach
//    void init() {
//        UserProfile userProfile = new UserProfile("a",1, GenderType.FEMALE);
//        UserProfile userProfile2 = new UserProfile("b",1, GenderType.FEMALE);
//        UserProfile userProfile3 = new UserProfile("c",1, GenderType.FEMALE);
//        user = new User(1L,"user", "aaa", userProfile);
//        targetUser = new User(2L,"targetUser", "aaa", userProfile2);
//        user3 = new User(3L,"user3", "aaa", userProfile3);
//        page = new Page(1L, user, "내용");
//        comment = new Comment(1L, user, page, "내용");
//    }
//
//    @Nested
//    @DisplayName("회원가입")
//    class signUpTest {
//
//        @Test
//        @DisplayName("중복X")
//        void noDuplicationTest() {
//
//            //given
//            given(userRepository.findUserByEmail(any(String.class))).willReturn(Optional.empty());
//            given(userRepository.save(any(User.class))).willReturn(new User("email", "password", new UserProfile("nickname",25,GenderType.FEMALE)));
//            given(userActiveRepository.findByEmail(any(String.class))).willReturn(Optional.of(new UserActive("email", true)));
//            UserSignUpDto userSignUpDto = UserSignUpDto.builder()
//                    .email("email")
//                    .password("password")
//                    .nickname("nickname")
//                    .age(25)
//                    .gender(GenderType.FEMALE)
//                    .build();
//            userActiveRepository.save(new UserActive(userSignUpDto.getEmail(), true));
//
//            // when
//            UserSignUpDto signUpDto = userAuthService.signUp(userSignUpDto);
//
//
//            // then
//            assertThat(signUpDto.getEmail()).isEqualTo(userSignUpDto.getEmail());
//            assertThat(signUpDto.getPassword()).isEqualTo(userSignUpDto.getPassword());
//            assertThat(signUpDto.getNickname()).isEqualTo(userSignUpDto.getNickname());
//        }
//
//        @Test
//        @DisplayName("중복O")
//        void duplicationTest() {
//
//            //given
//            given(userRepository.findUserByEmail(any(String.class))).willReturn(Optional.of(user));
//            UserSignUpDto userSignUpDto = UserSignUpDto.builder()
//                    .email("user")
//                    .password("password")
//                    .nickname("nickname")
//                    .age(25)
//                    .gender(GenderType.FEMALE)
//                    .build();
//
//
//            // when
//            // then
//            assertThrows(UserDuplicateException.class, () -> userAuthService.signUp(userSignUpDto));
//
//        }
//
//
//    }
//
//}