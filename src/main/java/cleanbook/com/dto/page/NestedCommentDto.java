package cleanbook.com.dto.page;

import cleanbook.com.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NestedCommentDto {

    private UserDto userDto;
    private Long commentId;
    private String content;
    private int group;
    private int likeCount;
    private boolean like;
    private LocalDateTime createdDate;

    public NestedCommentDto(UserDto userDto, Long commentId, String content, int group, int likeCount, LocalDateTime createdDate) {
        this.userDto = userDto;
        this.commentId = commentId;
        this.content = content;
        this.group = group;
        this.likeCount = likeCount;
        this.createdDate = createdDate;
    }
}
