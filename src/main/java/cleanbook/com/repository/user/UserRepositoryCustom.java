package cleanbook.com.repository.user;

import cleanbook.com.domain.user.UserDto;

import java.util.List;

public interface UserRepositoryCustom {
    List<UserDto> findUsersStartWithNickname(String nickname);
}
