package cleanbook.com.service;

import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.page.PageDetailDto;
import cleanbook.com.repository.page.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PageService {

    private final PageRepository pageRepository;


    // 게시글 생성
    public void createPage(Page page) {
        pageRepository.save(page);
    }

    // 게시글 상세보기
    /*
    (작성자id, 작성자 닉네임, 작성자 프로필이미지, 이미지, 글 내용, 좋아요 수 , 작성시간,
    댓글(작성자 id, 작성자 닉네임, 작성자 프로필이미지, 댓글 내용, 좋아요 수, 작성시간)
     */
    public PageDetailDto readPage(Long pageId) {
        return pageRepository.readPageDetail(pageId);
    }


   // 메인페이지 게시글 조회(내가 팔로우 한 사람만, 시간순)

    // 유저 게시글 조회(특정 유저의 게시글 전체, 시간순)

    // 게시글 수정

    // 게시글 삭제
}
