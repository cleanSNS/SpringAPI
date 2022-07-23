package cleanbook.com.dto.user;

import cleanbook.com.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
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
