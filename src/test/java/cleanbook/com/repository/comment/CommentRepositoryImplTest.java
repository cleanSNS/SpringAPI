package cleanbook.com.repository.comment;

import cleanbook.com.config.QuerydslConfig;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.CommentDto;
import cleanbook.com.dto.page.NestedCommentDto;
import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.page.Comment;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.UserProfile;
import cleanbook.com.exception.exceptions.CommentNotFoundException;
import cleanbook.com.exception.exceptions.NoMoreCommentException;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(QuerydslConfig.class)
class CommentRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private CommentRepository commentRepository;


    Long pageId;
    Long pageId2;

    @BeforeEach
    void init() {
        int group = 0;
        UserProfile userProfile = new UserProfile("name", 25, GenderType.FEMALE);
        User user = User.builder().userProfile(userProfile).build();
        userRepository.save(user);
        Page page = new Page(user, "글내용");
        Page page2 = new Page(user, "글내용2");
        pageRepository.save(page);
        pageRepository.save(page2);
        pageId = page.getId();
        pageId2 = page2.getId();

        for (int j = 0; j < 15; j++) {
            Comment comment = Comment.builder()
                    .user(user)
                    .page(page)
                    .visible(true)
                    .content(String.valueOf(j+1))
                    .filteredContent(String.valueOf(j+1))
                    .nested(false)
                    .group(++group)
                    .build();

            commentRepository.save(comment);

            int order = 1;
            for (int k = 0; k < 5; k++) {
                Comment nestedComment = Comment.builder()
                        .user(user)
                        .page(page)
                        .visible(true)
                        .content(String.valueOf((j+1)*10+k+1))
                        .filteredContent(String.valueOf((j+1)*10+k+1))
                        .nested(true)
                        .group(group)
                        .order(order++)
                        .build();
                commentRepository.save(nestedComment);
            }
        }
    }

    @Nested
    @DisplayName("댓글 조회")
    class readCommentList{


        @Test
        @DisplayName("댓글 조회")
        public void readCommentListFirstTest() {

            // when
            ResultDto<List<CommentDto>> resultDto = commentRepository.readCommentList(1L, pageId, 1L, 10);
            List<CommentDto> commentDtoList = resultDto.getData();
            Long startPageId = resultDto.getStartId();

            // then
            assertThat(commentDtoList).extracting("content").contains("1","2","9","10");
            assertThat(commentDtoList.size()).isEqualTo(10);
        }

        @Test
        @DisplayName("2번째 페이지 댓글 조회")
        public void readCommentListSecondTest() {

            // when
            ResultDto<List<CommentDto>> resultDto = commentRepository.readCommentList(1L, pageId, 1L, 10);
            List<CommentDto> commentDtoList = resultDto.getData();
            Long startPageId = resultDto.getStartId();

            resultDto = commentRepository.readCommentList(1L, pageId, startPageId, 10);
            commentDtoList = resultDto.getData();
            startPageId = resultDto.getStartId();

            // then
            assertThat(commentDtoList.size()).isEqualTo(5);
            assertThat(commentDtoList).extracting("content").contains("11","15");

        }

        @Test
        @DisplayName("더이상 조회할 댓글이 없음")
        public void readCommentListThirdTest() {

            // when
            // then
            ResultDto<List<CommentDto>> resultDto = commentRepository.readCommentList(1L, pageId, 999999L, 10);
            List<CommentDto> commentDtoList = resultDto.getData();

            assertThat(commentDtoList.size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("대댓글 조회")
    class readNestedCommentList{

        @Test
        @DisplayName("대댓글 조회")
        void readNestedCommentListTest() {

            // when
            ResultDto<List<NestedCommentDto>> resultDto = commentRepository.readNestedCommentList(1L, pageId, 1, 1L, 10);
            List<NestedCommentDto> commentDtoList = resultDto.getData();
            Long startPageId = resultDto.getStartId();

            // then
            assertThat(commentDtoList).extracting("content").containsExactly("11","12","13","14","15");
            assertThat(commentDtoList.size()).isEqualTo(5);
        }

        @Test
        @DisplayName("더이상 조회할 댓글이 없음")
        public void readCommentListThirdTest() {

            // when
            // then
            ResultDto<List<NestedCommentDto>> resultDto = commentRepository.readNestedCommentList(1L, pageId, 1, 999999L, 10);
            List<NestedCommentDto> commentDtoList = resultDto.getData();

            assertThat(commentDtoList.size()).isEqualTo(0);
        }
    }

    @Test
    @DisplayName("게시글의 마지막 댓글그룹 가져오기")
    void getLastGroup() {


        // when
        int group = commentRepository.findFirstByPage_IdOrderByGroupDesc(pageId).orElseThrow(CommentNotFoundException::new).getGroup();


        // then
        assertThat(group).isEqualTo(15);

    }
}