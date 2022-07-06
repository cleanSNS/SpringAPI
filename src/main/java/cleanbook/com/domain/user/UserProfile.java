package cleanbook.com.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

    @NotEmpty
    private String nickname;
    @NotEmpty
    private int age;
    @NotEmpty
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private String imgUrl;
    private String selfIntroduce;

    public UserProfile(String nickname, int age, GenderType gender) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
    }
}
