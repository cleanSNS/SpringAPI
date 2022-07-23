package cleanbook.com.entity.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccessToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    private long refresh_token_expires_in;

}
