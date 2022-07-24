package cleanbook.com.service;

import cleanbook.com.config.QuerydslConfig;
import cleanbook.com.dto.user.BlockedUserDto;
import cleanbook.com.dto.user.UserSignUpDto;
import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.enums.ReportType;
import cleanbook.com.entity.page.Comment;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.UserProfile;
import cleanbook.com.entity.user.block.Block;
import cleanbook.com.entity.user.like.LikeComment;
import cleanbook.com.entity.user.like.LikePage;
import cleanbook.com.entity.user.like.LikeType;
import cleanbook.com.entity.user.report.ReportComment;
import cleanbook.com.entity.user.report.ReportPage;
import cleanbook.com.entity.user.report.ReportUser;
import cleanbook.com.exception.exceptions.UserDuplicateException;
import cleanbook.com.repository.CommentRepository;
import cleanbook.com.repository.FollowRepository;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.BlockRepository;
import cleanbook.com.repository.user.UserRepository;
import cleanbook.com.repository.user.like.LikeCommentRepository;
import cleanbook.com.repository.user.like.LikePageRepository;
import cleanbook.com.repository.user.report.ReportCommentRepository;
import cleanbook.com.repository.user.report.ReportPageRepository;
import cleanbook.com.repository.user.report.ReportUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Import(QuerydslConfig.class)
@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAuthService userAuthService;

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
        page = new Page(1L, user, "내용");
        comment = new Comment(1L, user, page, "내용");
    }

    @Nested
    @DisplayName("회원가입")
    class signUpTest {

        @Test
        @DisplayName("중복X")
        void noDuplicationTest() {

            //given
            given(userRepository.findUserByEmail(any(String.class))).willReturn(Optional.empty());
            given(userRepository.save(any(User.class))).willReturn(new User("email", "password", new UserProfile("nickname",25,GenderType.FEMALE)));
            UserSignUpDto userSignUpDto = UserSignUpDto.builder()
                    .email("email")
                    .password("password")
                    .nickname("nickname")
                    .age(25)
                    .gender(GenderType.FEMALE)
                    .build();

            // when
            UserSignUpDto signUpDto = userAuthService.signUp(userSignUpDto);


            // then
            assertThat(signUpDto.getEmail()).isEqualTo(userSignUpDto.getEmail());
            assertThat(signUpDto.getPassword()).isEqualTo(userSignUpDto.getPassword());
            assertThat(signUpDto.getNickname()).isEqualTo(userSignUpDto.getNickname());
        }

        @Test
        @DisplayName("중복O")
        void duplicationTest() {

            //given
            given(userRepository.findUserByEmail(any(String.class))).willReturn(Optional.of(user));
            UserSignUpDto userSignUpDto = UserSignUpDto.builder()
                    .email("user")
                    .password("password")
                    .nickname("nickname")
                    .age(25)
                    .gender(GenderType.FEMALE)
                    .build();


            // when
            // then
            assertThrows(UserDuplicateException.class, () -> userAuthService.signUp(userSignUpDto));

        }


    }

}