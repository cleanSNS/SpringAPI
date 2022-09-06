package cleanbook.com.dto.user;

import cleanbook.com.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    @Email
    @NotBlank
    private String email;
    private String password;

    public UserLoginDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
