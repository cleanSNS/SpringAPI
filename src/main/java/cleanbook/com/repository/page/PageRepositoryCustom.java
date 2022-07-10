package cleanbook.com.repository.page;

import cleanbook.com.domain.ResultDto;
import cleanbook.com.domain.page.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PageRepositoryCustom {

    PageDetailDto readPageDetail(Long pageId);
    PageDto readPageDto(Long pageId);
    List<String> readPageImgUrlList(Long pageId);
    ResultDto<List<CommentDto>> readPageCommentList(Long pageId, Pageable pageable);
    ResultDto<List<MainPageDto>> readFolloweePageList(Long userId, Long startPageId, int pageSize);
    ResultDto<List<UserPageDto>> readUserPageList(Long userId, Long pageId, int pageSize);
    void testQuery(Long userId);
}
