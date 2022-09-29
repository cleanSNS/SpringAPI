package cleanbook.com.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNicknameProfileDto {
    private String nickname;
    private String imgUrl;
    private String selfIntroduction;
    private int followerCount;
    private int followeeCount;
}
