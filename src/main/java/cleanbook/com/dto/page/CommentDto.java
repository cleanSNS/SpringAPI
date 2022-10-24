package cleanbook.com.dto.page;

import cleanbook.com.dto.user.UserDto;
import cleanbook.com.entity.page.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static cleanbook.com.dto.user.UserDto.createUserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private UserDto userDto;
    private Long commentId;
    private String content;
    private int group;
    private int likeCount;
    private boolean like;
    private LocalDateTime createdDate;

    public CommentDto(UserDto userDto, Long commentId, String content, int group, int likeCount, LocalDateTime createdDate) {
        this.userDto = userDto;
        this.commentId = commentId;
        this.content = content;
        this.group = group;
        this.likeCount = likeCount;
        this.createdDate = createdDate;
    }

    public static CommentDto createCommentDto(Comment comment) {
        return CommentDto.builder()
                .userDto(createUserDto(comment.getUser()))
                .commentId(comment.getId())
                .content(comment.getContent())
                .group(comment.getGroup())
                .createdDate(comment.getCreatedDate())
                .build();
    }
}
