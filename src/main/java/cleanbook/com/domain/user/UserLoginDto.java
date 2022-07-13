package cleanbook.com.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    private String email;
    private String password;

    public UserLoginDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
