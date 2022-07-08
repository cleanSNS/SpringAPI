package cleanbook.com.repository.page;

import cleanbook.com.domain.page.CommentDto;
import cleanbook.com.domain.page.PageDetailDto;
import cleanbook.com.domain.page.PageDto;

import java.util.List;

public interface PageRepositoryCustom {

    PageDetailDto readPageDetail(Long pageId);
    PageDto readPageDto(Long pageId);
    List<String> readPageImgUrlList(Long pageId);
    List<CommentDto> readPageCommentList(Long pageId);
}
