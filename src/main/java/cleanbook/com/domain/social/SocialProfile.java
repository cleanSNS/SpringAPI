package cleanbook.com.domain.social;

import cleanbook.com.domain.user.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialProfile {
    private String id;
    private String email;
    private String nickname;
    private GenderType gender;
}
