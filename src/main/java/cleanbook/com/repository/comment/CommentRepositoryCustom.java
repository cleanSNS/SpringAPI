package cleanbook.com.repository.comment;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.CommentDto;

import java.util.List;

public interface CommentRepositoryCustom {
    ResultDto<List<CommentDto>> readCommentList(Long pageId, Long startId, int pageSize);
    ResultDto<List<CommentDto>> readNestedCommentList(Long pageId, int group, Long startId, int pageSize);
}
