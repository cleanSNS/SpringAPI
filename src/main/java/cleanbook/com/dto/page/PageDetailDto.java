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
    private List<String> imgUrlList = new ArrayList<>();
    private List<String> hashtagList = new ArrayList<>();
    private ResultDto<List<CommentDto>> commentDtoList;
    private boolean like;

    public PageDetailDto(PageDto pageDto, List<String> imgUrlList, List<String> hashtagList) {
        this.pageDto = pageDto;
        this.imgUrlList = imgUrlList;
        this.hashtagList = hashtagList;
    }

    public static PageDetailDto createPageDetailDto(Page page) {
        return PageDetailDto.builder()
                .pageDto(PageDto.createPageDto(page))
                .imgUrlList(page.getPageImgUrlList().stream().map(PageImgUrl::getImgUrl).collect(Collectors.toList()))
                .hashtagList(page.getPageHashtagList().stream().map(p -> p.getHashtag().getName()).collect(Collectors.toList()))
                .commentDtoList(new ResultDto<>(page.getCommentList().stream().map(CommentDto::createCommentDto).collect(Collectors.toList())))
                .build();
    }
}
