package cleanbook.com.dto.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageDto {
    private Long pageId;
    private int likeCount;
    private boolean likeReadAuth;
    private String imgUrl;
}
