package cleanbook.com.controller.local;

import cleanbook.com.dto.CountDto;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.*;
import cleanbook.com.exception.Response;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/local/page")
public class LocalPageController {

    private final PageService pageService;
    private final TokenProvider tokenProvider;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<Response> createPage(@CookieValue("X-AUTH-TOKEN") String token, @Validated @RequestBody PageCreateDto pageCreateDto) {
        Long userId = tokenProvider.getUserId(token);
        pageService.createPage(userId, pageCreateDto);
        return ResponseEntity.ok(new Response("success"));
    }

    // 게시글 상세보기
    @GetMapping("/{pageId}/detail")
    public ResponseEntity<ResultDto<PageDetailDto>> readPageDetail(@CookieValue(value = "X-AUTH-TOKEN", required = false) String token,
                                                                   @PathVariable Long pageId) {
        Long userId = tokenProvider.getUserId(token);
        return ResponseEntity.ok(pageService.readPageDetail(userId, pageId));
    }

    // 메인페이지 게시글 조회(내가 팔로우 한 사람만, 시간순)
    @GetMapping("/main")
    public ResponseEntity<ResultDto<List<MainPageDto>>> readPageList(@CookieValue("X-AUTH-TOKEN") String token,
                                                                     @RequestParam Long startId) {
        Long userId = tokenProvider.getUserId(token);
        return ResponseEntity.ok(pageService.readPageList(userId, startId));
    }

    // 유저 게시글 조회(특정 유저의 게시글 전체, 시간순)
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResultDto<List<UserPageDto>>> readUserPageList(@CookieValue(value = "X-AUTH-TOKEN", required = false) String token,
                                                                         @PathVariable Long userId, @RequestParam Long startId) {
        Long myUserId = tokenProvider.getUserId(token);
        return ResponseEntity.ok(pageService.readUserPageList(myUserId, userId, startId));
    }

    // 게시글 수정
    @PutMapping("/{pageId}")
    public ResponseEntity<Response> updatePage(@CookieValue("X-AUTH-TOKEN") String token, @PathVariable Long pageId,
                                                                   @RequestBody PageUpdateDto pageUpdateDto) {
        Long userId = tokenProvider.getUserId(token);
        pageService.updatePage(userId, pageId, pageUpdateDto);
        return ResponseEntity.ok(new Response("success"));
    }

    // 게시글 삭제
    @DeleteMapping("/{pageId}")
    public ResponseEntity<Response> deletePage(@CookieValue("X-AUTH-TOKEN") String token, @PathVariable Long pageId) {
        Long userId = tokenProvider.getUserId(token);
        pageService.deletePage(userId, pageId);
        return ResponseEntity.ok(new Response("success"));
    }

    // 해시태그 검색
    @GetMapping("/search/hashtag")
    public ResultDto<List<UserPageDto>> readPageListByHashtag(@RequestParam String hashtag, @RequestParam Long startId) {
        return pageService.readPageListByHashtag(startId, hashtag);
    }

    // 해시태그 검색수
    @GetMapping("/search/hashtag/count")
    public ResultDto<CountDto> getPageListCountByHashtag(@RequestParam String hashtag) {
        return pageService.getPageListCountByHashtag(hashtag);
    }
}




