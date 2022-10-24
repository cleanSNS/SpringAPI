package cleanbook.com.service;

import cleanbook.com.dto.CountDto;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.*;
import cleanbook.com.entity.page.*;
import cleanbook.com.entity.user.User;
import cleanbook.com.exception.exceptions.*;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.BlockRepository;
import cleanbook.com.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cleanbook.com.util.AIUtils.filterContent;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PageService {

    private final PageRepository pageRepository;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final AwsS3Service awsS3Service;

    // 게시글 생성
    public Page createPage(Long userId ,PageCreateDto pageCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Page page = Page.createPage(user, pageCreateDto);

        // 인공지능 욕설 필터링
        String filteredContent = filterContent(pageCreateDto.getContent());
        page.updateFilteredContent(filteredContent);

        return pageRepository.save(page);
    }

    // 게시글 상세보기
    public ResultDto<PageDetailDto> readPageDetail(Long userId, Long pageId) {
        Page page = pageRepository.findById(pageId).orElseThrow(PageNotFoundException::new);

        // 차단했거나 당했다면 볼 수 없음
        if (blockRepository.findByUser_IdAndTargetUser_Id(userId, page.getUser().getId()).isPresent() ||
            blockRepository.findByUser_IdAndTargetUser_Id(page.getUser().getId(), userId).isPresent()) {
            throw new MyException("볼 수 없는 게시글입니다.");
        }

        return new ResultDto<>(pageRepository.readPageDetail(userId, pageId));
    }

   // 메인페이지 게시글 조회(내가 팔로우 한 사람만, 시간순)
    public ResultDto<List<MainPageDto>> readPageList(Long userId, Long startId) {
        return pageRepository.readFolloweePageList(userId, startId, 10);
    }

    // 유저 게시글 조회(특정 유저의 게시글 전체, 시간순)
    public ResultDto<List<UserPageDto>> readUserPageList(Long userId, Long targetUserId, Long startId) {

        // 차단했거나 당했다면 볼 수 없음
        if (blockRepository.findByUser_IdAndTargetUser_Id(userId, targetUserId).isPresent() ||
            blockRepository.findByUser_IdAndTargetUser_Id(targetUserId, userId).isPresent()) {
            return new ResultDto<>(new ArrayList<>());
        }

        return pageRepository.readUserPageList(userId, targetUserId, startId, 9);
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

        // page 이미지 삭제
        if (page.getImgUrlList() != null) {
            awsS3Service.deleteFiles(page.getImgUrlList().stream().map(PageImgUrl::getImgUrl).collect(Collectors.toList()));
        }
        user.getPageList().remove(page);
        pageRepository.delete(page);
    }


    // 해시태그 검색
    @Transactional(readOnly = true)
    public ResultDto<List<UserPageDto>> readPageListByHashtag(Long startId, String keyword) {
        if (hasText(keyword)) {
            return pageRepository.readPageByHashtag(keyword, startId, 9);
        }
        else {
            throw new EmptyStringException();
        }
    }

    // 해시태그 개수 검색
    @Transactional(readOnly = true)
    public ResultDto<CountDto> getPageListCountByHashtag(String hashtagName) {
        if (hasText(hashtagName)) {
            return pageRepository.getPageListCountByHashtag(hashtagName);
        }
        else {
            throw new EmptyStringException();
        }
    }
}
