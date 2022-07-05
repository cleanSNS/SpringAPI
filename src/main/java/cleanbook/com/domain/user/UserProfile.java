package cleanbook.com.domain.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

    @NotEmpty
    private String name;
    @NotEmpty
    private int age;
    @NotEmpty
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private String imgUrl;
    private String selfIntroduce;

    public UserProfile(String name, int age, GenderType gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
}
