package cleanbook.com.domain.user;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long userId;
    private String nickname;
    private String imgUrl;
}
