package cleanbook.com.domain.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageDetailDto {

    private PageDto pageDto;
    private List<String> imgUrlList;
    private List<CommentDto> commentDtoList;
}
