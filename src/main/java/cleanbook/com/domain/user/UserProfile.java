package cleanbook.com.domain.user;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

@Embeddable
public class UserProfile {

    @NotEmpty
    private String name;
    @NotEmpty
    private int age;
    @NotEmpty
    private int gender;
    private String imgUrl;
    private String selfIntroduce;
}
