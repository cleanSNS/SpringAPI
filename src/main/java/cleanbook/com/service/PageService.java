package cleanbook.com.service;

import cleanbook.com.domain.ResultDto;
import cleanbook.com.domain.page.*;
import cleanbook.com.domain.user.User;
import cleanbook.com.exception.NoAuthroizationException;
import cleanbook.com.exception.PageNotFoundException;
import cleanbook.com.exception.UserNotFoundException;
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

    // 게시글 생성
    public Page createPage(Long userId, PageCreateDto pageCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Page page = Page.createPage(user, pageCreateDto);
        return pageRepository.save(page);
    }

    // 게시글 상세보기
    public PageDetailDto readPageDetail(Long pageId) {
        return pageRepository.readPageDetail(pageId);
    }

   // 메인페이지 게시글 조회(내가 팔로우 한 사람만, 시간순)
    public ResultDto<List<MainPageDto>> readPageList(Long userId, Long startPageId, int pageSize) {
        return pageRepository.readFolloweePageList(userId, startPageId, pageSize);
    }

    // 유저 게시글 조회(특정 유저의 게시글 전체, 시간순)
    public ResultDto<List<UserPageDto>> readUserPageList(Long userId, Long startPageId, int pageSize) {
        return pageRepository.readUserPageList(userId, startPageId, pageSize);
    }

    // 게시글 수정
    public Page updatePage(Long userId, Long pageId, PageUpdateDto pageUpdateDto) {
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFoundException::new);

        // 권한이 없을 경우
        if (!page.getUser().getId().equals(userId)) {
            throw new NoAuthroizationException();
        }

        page.updatePage(pageUpdateDto);
        return page;
    }

    // 게시글 삭제
    public Long deletePage(Long userId, Long pageId) {
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 권한이 없을 경우
        if (!page.getUser().getId().equals(userId)) {
            throw new NoAuthroizationException();
        }

        user.getPageList().remove(page);
        pageRepository.delete(page);
        return pageId;
    }
}
