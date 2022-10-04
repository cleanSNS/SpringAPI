package cleanbook.com.repository.user;

import cleanbook.com.config.QuerydslConfig;
import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.UserProfile;
import cleanbook.com.dto.user.UserDto;
import cleanbook.com.entity.user.block.Block;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static cleanbook.com.entity.user.block.Block.createBlock;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlockRepository blockRepository;


    @Nested
    @DisplayName("유저 검색")
    class searchUser{

        @Test
        @DisplayName("차단x")
        void none() {

            //given
            UserProfile userProfile = new UserProfile("lacram", 5, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            userRepository.save(user);



            // when
            List<UserDto> userDtoList = userRepository.findUsersStartWithNickname(1L, "lac").getData();


            // then
            assertThat(userDtoList.size()).isEqualTo(1);

        }

        @Test
        @DisplayName("차단한 경우")
        void blockedUser() {

            //given
            UserProfile userProfile = new UserProfile("abc", 5, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            userRepository.save(user);
            UserProfile userProfile2 = new UserProfile("def", 5, GenderType.FEMALE);
            User user2 = new User("bb", "bb", userProfile);
            userRepository.save(user2);

            blockRepository.save(createBlock(user,user2));

            // when
            List<UserDto> userDtoList = userRepository.findUsersStartWithNickname(user.getId(), "de").getData();


            // then
            assertThat(userDtoList.size()).isEqualTo(0);

        }

        @Test
        @DisplayName("차단당한 경우")
        void blockUser() {

            //given
            UserProfile userProfile = new UserProfile("abc", 5, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            userRepository.save(user);
            UserProfile userProfile2 = new UserProfile("def", 5, GenderType.FEMALE);
            User user2 = new User("bb", "bb", userProfile);
            userRepository.save(user2);

            blockRepository.save(createBlock(user2,user));

            // when
            List<UserDto> userDtoList = userRepository.findUsersStartWithNickname(user.getId(), "de").getData();


            // then
            assertThat(userDtoList.size()).isEqualTo(0);

        }
    }
}