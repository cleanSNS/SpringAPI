package cleanbook.com.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedUserDto {
    private Long userId;
    private String nickname;
    private String imgUrl;
}
