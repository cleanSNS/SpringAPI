package cleanbook.com.controller;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.CommentCreateDto;
import cleanbook.com.dto.page.CommentDto;
import cleanbook.com.exception.Response;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/page")
public class CommentController {

    private final CommentService commentService;
    private final TokenProvider tokenProvider;

    // 댓글 생성
    @PostMapping("/{pageId}/comment")
    public ResponseEntity<Response> createComment(@CookieValue("X-AUTH-TOKEN") String token, @Validated @RequestBody CommentCreateDto commentCreateDto) {
        Long userId = tokenProvider.getUserId(token);
        commentService.createComment(userId, commentCreateDto);
        return ResponseEntity.ok(new Response("success"));
    }

    // 댓글 보기
    @GetMapping("/{pageId}/comment")
    public ResponseEntity<ResultDto<List<CommentDto>>> readCommentList(@PathVariable Long pageId, @RequestParam Long startId) {
        ResultDto<List<CommentDto>> resultDto = commentService.readCommentList(pageId, startId);
        return ResponseEntity.ok(resultDto);
    }

    // 대댓글 보기
    @GetMapping("/{pageId}/nested")
    public ResponseEntity<ResultDto<List<CommentDto>>> readNestedCommentList(@PathVariable Long pageId, @RequestParam int group, @RequestParam Long startId) {
        ResultDto<List<CommentDto>> resultDto = commentService.readNestedCommentList(pageId, group, startId);
        return ResponseEntity.ok(resultDto);
    }

    // 댓글 삭제
    @DeleteMapping("/{pageId}/comment/{commentId}")
    public ResponseEntity<Response> deleteComment(@PathVariable Long commentId, @CookieValue("X-AUTH-TOKEN") String token) {
        Long userId = tokenProvider.getUserId(token);
        commentService.deleteComment(userId,commentId);
        return ResponseEntity.ok(new Response("success"));
    }
}






