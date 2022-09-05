package cleanbook.com.dto.user;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String nickname;
    private String imgUrl;
}
