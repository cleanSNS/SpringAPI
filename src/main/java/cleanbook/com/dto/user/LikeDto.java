package cleanbook.com.dto.user;

import cleanbook.com.entity.user.like.LikeType;
import lombok.Data;

@Data
public class LikeDto {
    private Long targetId;
    private LikeType type;
}
