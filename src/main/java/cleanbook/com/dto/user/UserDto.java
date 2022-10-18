package cleanbook.com.dto.user;

import cleanbook.com.entity.user.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String nickname;
    private String imgUrl;

    public static UserDto createUserDto(User user) {
        return UserDto.builder()
                .userId(user.getId())
                .nickname(user.getUserProfile().getNickname())
                .imgUrl(user.getUserProfile().getImgUrl())
                .build();
    }
}
