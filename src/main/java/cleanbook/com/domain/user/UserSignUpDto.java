package cleanbook.com.domain.user;

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
    private int age;
    private GenderType gender;

    public UserSignUpDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getUserProfile().getNickname();
        this.age = user.getUserProfile().getAge();
        this.gender = user.getUserProfile().getGender();
    }
}
