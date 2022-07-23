package cleanbook.com.dto.user;

import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpDto {
    private String email;
    private String password;
    private String nickname;
    private Integer age;
    private GenderType gender;

    public UserSignUpDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getUserProfile().getNickname();
        this.age = user.getUserProfile().getAge();
        this.gender = user.getUserProfile().getGender();
    }
}
