package cleanbook.com.dto.user;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class UserPasswordDto {
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")
    private String password;
}
