package cleanbook.com.dto.user;

import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.user.User;
import cleanbook.com.validation.ValidEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpDto {

    @NotBlank
    @Email
    private String email;

    // 8~20자 영어, 숫자, 특수문자
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")
    private String password;

    @NotBlank
    private String nickname;

    @Max(value = 150)
    @Min(value = 1)
    private Integer age;

    @ValidEnum(enumClass = GenderType.class)
    private GenderType gender;

    @NotNull
    private boolean ageVisible;

    @NotNull
    private boolean genderVisible;

    public UserSignUpDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getUserProfile().getNickname();
        this.age = user.getUserProfile().getAge();
        this.gender = user.getUserProfile().getGender();
        this.ageVisible  = user.getUserProfile().isAgeVisible();
        this.genderVisible = user.getUserProfile().isGenderVisible();
    }
}
