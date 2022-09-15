package cleanbook.com.entity.user;

import cleanbook.com.dto.user.UserProfileDto;
import cleanbook.com.entity.enums.GenderType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

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

    @Column(columnDefinition = "TEXT")
    private String imgUrl;
    private String selfIntroduction;

    public UserProfile(String nickname, int age, GenderType gender) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
    }

    public static UserProfile createUserProfile(UserProfileDto dto) {
        return UserProfile.builder()
                .nickname(dto.getNickname())
                .age(dto.getAge())
                .ageVisible(dto.isAgeVisible())
                .gender(dto.getGender())
                .genderVisible(dto.isGenderVisible())
                .imgUrl(dto.getImgUrl())
                .selfIntroduction(dto.getSelfIntroduction())
                .build();
    }
}
