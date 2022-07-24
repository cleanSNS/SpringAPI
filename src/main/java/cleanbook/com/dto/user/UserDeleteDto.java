package cleanbook.com.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDeleteDto {
    @NotNull
    Long userId;
    @NotBlank
    String password;
}
