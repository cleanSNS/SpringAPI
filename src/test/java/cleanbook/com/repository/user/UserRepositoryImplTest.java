package cleanbook.com.repository.user;

import cleanbook.com.configuration.QuerydslConfig;
import cleanbook.com.domain.user.GenderType;
import cleanbook.com.domain.user.User;
import cleanbook.com.domain.user.UserProfile;
import cleanbook.com.domain.user.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {

        for (int i = 0; i < 101; i++) {
            UserProfile userProfile = new UserProfile(Integer.toString(i), i, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            userRepository.save(user);
        }
    }

    @Test
    @DisplayName("유저_검색")
    void findUsersTest() {
    
        //given

        
        // when
        List<UserDto> userDtoList = userRepository.findUsersStartWithNickname("10");


        // then
        assertThat(userDtoList.size()).isEqualTo(2);
        assertThat(userDtoList).extracting("nickname").containsExactly("10","100");
        
    }
}