package cleanbook.com.service;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.*;
import cleanbook.com.entity.page.*;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.NoAuthroizationException;
import cleanbook.com.exception.exceptions.PageNotFoundException;
import cleanbook.com.exception.exceptions.UserNotFoundException;
import cleanbook.com.jwt.TokenProvider;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PageService {

    private final PageRepository pageRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    // 게시글 생성
    public Page createPage(Long userId ,PageCreateDto pageCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Page page = Page.createPage(user, pageCreateDto);
        return pageRepository.save(page);
    }

    // 게시글 상세보기
    public ResultDto<PageDetailDto> readPageDetail(Long pageId) {
        pageRepository.findById(pageId).orElseThrow(PageNotFoundException::new);
        return new ResultDto<>(pageRepository.readPageDetail(pageId));
    }

   // 메인페이지 게시글 조회(내가 팔로우 한 사람만, 시간순)
    public ResultDto<List<MainPageDto>> readPageList(Long userId, Long startId) {
        return pageRepository.readFolloweePageList(userId, startId, 10);
    }

    // 유저 게시글 조회(특정 유저의 게시글 전체, 시간순)
    public ResultDto<List<UserPageDto>> readUserPageList(Long userId, Long startId) {
        return pageRepository.readUserPageList(userId, startId, 10);
    }

    // 게시글 수정
    public void updatePage(Long userId, Long pageId, PageUpdateDto pageUpdateDto) {
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFoundException::new);

        // 권한이 없을 경우
        if (!page.getUser().getId().equals(userId)) {
            throw new NoAuthroizationException();
        }

        page.updatePage(pageUpdateDto);
    }

    // 게시글 삭제
    public void deletePage(Long userId, Long pageId) {
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 권한이 없을 경우
        if (!page.getUser().getId().equals(userId)) {
            throw new NoAuthroizationException();
        }

        user.getPageList().remove(page);
        pageRepository.delete(page);
    }
}
