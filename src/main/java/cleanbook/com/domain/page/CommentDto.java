package cleanbook.com.domain.page;

import cleanbook.com.domain.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private UserDto userDto;
    private Long commentId;
    private String content;
    private int likeCount;
    private LocalDateTime createdDate;
}
