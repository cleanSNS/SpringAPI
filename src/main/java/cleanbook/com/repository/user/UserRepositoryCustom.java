package cleanbook.com.repository.user;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.user.UserDto;

import java.util.List;

public interface UserRepositoryCustom {
    ResultDto<List<UserDto>> findUsersStartWithNickname(Long userId, String nickname);
}
