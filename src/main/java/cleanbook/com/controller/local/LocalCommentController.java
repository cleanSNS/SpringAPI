package cleanbook.com.controller.local;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.CommentCreateDto;
import cleanbook.com.dto.page.CommentDto;
import cleanbook.com.exception.Response;
import cleanbook.com.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/local/page")
public class LocalCommentController {

    private final CommentService commentService;

    @PostMapping("/{pageId}/comment")
    public ResponseEntity<Response> createComment(@RequestBody CommentCreateDto commentCreateDto) {
        commentService.createComment(commentCreateDto);
        return ResponseEntity.ok(new Response("success"));
    }

    @GetMapping("/{pageId}/comment")
    public ResponseEntity<ResultDto> readCommentList(@PathVariable Long pageId, @RequestParam Long startCommentId) {
        ResultDto<List<CommentDto>> resultDto = commentService.readCommentList(pageId, startCommentId);
        return ResponseEntity.ok(resultDto);
    }

    @GetMapping("/{pageId}/nested")
    public ResponseEntity<ResultDto> readNestedCommentList(@PathVariable Long pageId, @RequestParam int group, @RequestParam Long startCommentId) {
        ResultDto<List<CommentDto>> resultDto = commentService.readNestedCommentList(pageId, group, startCommentId);
        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/{pageId}/comment/{commentId}")
    public ResponseEntity<Response> deleteComment(@PathVariable Long commentId, @CookieValue("X-AUTH-TOKEN") String token) {
        commentService.deleteComment(token,commentId);
        return ResponseEntity.ok(new Response("success"));
    }
}






