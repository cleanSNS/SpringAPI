package cleanbook.com.dto.page;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.page.PageHashtag;
import cleanbook.com.entity.page.PageImgUrl;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDetailDto {
    private PageDto pageDto;
    private List<String> imgUrlList;
    private List<String> hashtagList;
    private ResultDto<List<CommentDto>> commentDtoList;
    private boolean like;

}
