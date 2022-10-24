package cleanbook.com.dto.page;

import cleanbook.com.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageDto {
    private UserDto userDto;
    private Long pageId;
    private String content;
    private String filteredContent;
    private int likeCount;
    private boolean likeReadAuth;
    private LocalDateTime createdDate;
}
