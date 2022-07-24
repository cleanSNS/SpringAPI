package cleanbook.com.entity.user;

import cleanbook.com.entity.enums.GenderType;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @NotBlank
    private String nickname;
    private Integer age;
    private boolean ageVisible;
    @NotBlank
    @Enumerated(EnumType.STRING)
    private GenderType gender;
    private boolean genderVisible;

    private String imgUrl;
    private String selfIntroduce;

    public UserProfile(String nickname, int age, GenderType gender) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
    }

    public void changeUserProfile(String nickname, int age, GenderType gender, String imgUrl, String selfIntroduce) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.imgUrl = imgUrl;
        this.selfIntroduce = selfIntroduce;
    }
}
