package cleanbook.com.dto.user;

import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.user.UserProfile;
import cleanbook.com.validation.ValidEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class UserProfileDto {

    @NotBlank
    private String nickname;

    @Max(value = 150)
    @Min(value = 1)
    private Integer age;

    @NotNull
    private boolean ageVisible;

    @ValidEnum(enumClass = GenderType.class)
    private GenderType gender;

    @NotNull
    private boolean genderVisible;

    private String imgUrl;
    private String selfIntroduction;

    public UserProfileDto(UserProfile userProfile) {
        this.nickname = userProfile.getNickname();
        this.age = userProfile.getAge();
        this.gender = userProfile.getGender();
        this.imgUrl = userProfile.getImgUrl();
        this.selfIntroduction = userProfile.getSelfIntroduction();
        this.ageVisible = userProfile.isAgeVisible();
        this.genderVisible = userProfile.isGenderVisible();
    }
}
