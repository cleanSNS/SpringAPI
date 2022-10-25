package cleanbook.com.repository.comment;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.CommentDto;
import cleanbook.com.dto.page.NestedCommentDto;

import java.util.List;

public interface CommentRepositoryCustom {
    ResultDto<List<CommentDto>> readCommentList(Long userId, Long pageId, Long startId, int pageSize);
    ResultDto<List<NestedCommentDto>> readNestedCommentList(Long userId, Long pageId, int group, Long startId, int pageSize);
}
