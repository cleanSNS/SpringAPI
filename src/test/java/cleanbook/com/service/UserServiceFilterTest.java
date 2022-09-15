//package cleanbook.com.service;
//
//import cleanbook.com.dto.ResultDto;
//import cleanbook.com.entity.page.Comment;
//import cleanbook.com.entity.page.Page;
//import cleanbook.com.entity.enums.GenderType;
//import cleanbook.com.entity.user.User;
//import cleanbook.com.entity.user.UserProfile;
//import cleanbook.com.entity.user.filter.Filter;
//import cleanbook.com.dto.user.UserDto;
//import cleanbook.com.repository.user.FilterRepository;
//import cleanbook.com.repository.user.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceFilterTest {
//
//    @Mock private UserRepository userRepository;
//    @Mock private FilterRepository filterRepository;
//
//    @InjectMocks private UserService userService;
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
//
//}
