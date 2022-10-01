package cleanbook.com.dto.page;

import cleanbook.com.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private UserDto userDto;
    private Long commentId;
    private String content;
    private int group;
    private int likeCount;
    private boolean like;
    private LocalDateTime createdDate;
}
