package cleanbook.com.dto.page;

import cleanbook.com.dto.user.UserDto;
import cleanbook.com.entity.page.Page;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PageDto {
    private UserDto userDto;
    private Long pageId;
    private String content;
    private Integer likeCount;
    private Boolean likeReadAuth;
    private LocalDateTime createdDate;

    public static PageDto createPageDto(Page page) {
        return PageDto.builder()
                .userDto(UserDto.createUserDto(page.getUser()))
                .pageId(page.getId())
                .content(page.getContent())
                .likeCount(page.getLikeCount())
                .likeReadAuth(page.getPageSetting().getLikeReadAuth())
                .createdDate(page.getCreatedDate())
                .build();
    }
}
