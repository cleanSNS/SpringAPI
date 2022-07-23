package cleanbook.com.entity.social;

import cleanbook.com.entity.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialProfile {
    private String email;
    private String nickname;
    private GenderType gender;
}
