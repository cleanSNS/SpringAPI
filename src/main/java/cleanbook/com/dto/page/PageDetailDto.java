package cleanbook.com.dto.page;

import cleanbook.com.dto.ResultDto;
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
    private List<String> hashtagList;
    private ResultDto<List<CommentDto>> commentDtoList;
}
