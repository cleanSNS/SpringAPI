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
    private String content;
    private int likeCount;
    private List<String> imgUrlList;
}
