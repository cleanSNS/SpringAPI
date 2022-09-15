//package cleanbook.com.repository.comment;
//
//import cleanbook.com.config.QuerydslConfig;
//import cleanbook.com.dto.ResultDto;
//import cleanbook.com.dto.page.CommentDto;
//import cleanbook.com.entity.enums.GenderType;
//import cleanbook.com.entity.page.Comment;
//import cleanbook.com.entity.page.Page;
//import cleanbook.com.entity.user.User;
//import cleanbook.com.entity.user.UserProfile;
//import cleanbook.com.exception.exceptions.NoMoreCommentException;
//import cleanbook.com.repository.page.PageRepository;
//import cleanbook.com.repository.user.UserRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.context.annotation.Import;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@DataJpaTest
//@Import(QuerydslConfig.class)
//class CommentRepositoryImplTest {
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PageRepository pageRepository;
//    @Autowired
//    private CommentRepository commentRepository;
//    @Autowired
//    private TestEntityManager em;
//
//    Long pageId;
//
//    @BeforeEach
//    void init() {
//        int group = 0;
//        UserProfile userProfile = new UserProfile("name", 25, GenderType.FEMALE);
//        User user = new User("aa", "aa", userProfile);
//        userRepository.save(user);
//        Page page = new Page(user, "글내용");
//        pageRepository.save(page);
//        pageId = page.getId();
//
//        for (int j = 0; j < 15; j++) {
//            Comment comment = Comment.builder()
//                    .user(user)
//                    .page(page)
//                    .visible(true)
//                    .content(String.valueOf(j+1))
//                    .nested(false)
//                    .group(++group)
//                    .build();
//
//            commentRepository.save(comment);
//
//            int order = 1;
//            for (int k = 0; k < 5; k++) {
//                Comment nestedComment = Comment.builder()
//                        .user(user)
//                        .page(page)
//                        .visible(true)
//                        .content(String.valueOf((j+1)*10+k+1))
//                        .nested(true)
//                        .group(group)
//                        .order(order++)
//                        .build();
//                commentRepository.save(nestedComment);
//            }
//        }
//    }
//
//    @Nested
//    @DisplayName("댓글 조회")
//    class readCommentList{
//
//
//        @Test
//        @DisplayName("댓글 조회")
//        public void readCommentListFirstTest() {
//
//            // when
//            ResultDto<List<CommentDto>> resultDto = commentRepository.readCommentList(pageId, 1L, 10);
//            List<CommentDto> commentDtoList = resultDto.getData();
//            Long startPageId = resultDto.getStartId();
//
//            // then
//            assertThat(commentDtoList).extracting("content").contains("1","2","9","10");
//            assertThat(commentDtoList.size()).isEqualTo(10);
//        }
//
//        @Test
//        @DisplayName("2번째 페이지 댓글 조회")
//        public void readCommentListSecondTest() {
//
//            // when
//            ResultDto<List<CommentDto>> resultDto = commentRepository.readCommentList(pageId, 1L, 10);
//            List<CommentDto> commentDtoList = resultDto.getData();
//            Long startPageId = resultDto.getStartId();
//
//            resultDto = commentRepository.readCommentList(pageId, startPageId, 10);
//            commentDtoList = resultDto.getData();
//            startPageId = resultDto.getStartId();
//
//            // then
//            assertThat(commentDtoList.size()).isEqualTo(5);
//            assertThat(commentDtoList).extracting("content").contains("11","15");
//
//        }
//
//        @Test
//        @DisplayName("더이상 조회할 댓글이 없음")
//        public void readCommentListThirdTest() {
//
//            // when
//            // then
//            assertThrows(NoMoreCommentException.class, () -> {
//                commentRepository.readCommentList(pageId, 99999L, 10);
//            });
//        }
//    }
//
//    @Nested
//    @DisplayName("대댓글 조회")
//    class readNestedCommentList{
//
//        @Test
//        @DisplayName("대댓글 조회")
//        void readNestedCommentListTest() {
//
//            // when
//            ResultDto<List<CommentDto>> resultDto = commentRepository.readNestedCommentList(pageId, 1, 1L, 10);
//            List<CommentDto> commentDtoList = resultDto.getData();
//            Long startPageId = resultDto.getStartId();
//
//            // then
//            assertThat(commentDtoList).extracting("content").containsExactly("11","12","13","14","15");
//            assertThat(commentDtoList.size()).isEqualTo(5);
//        }
//
//        @Test
//        @DisplayName("더이상 조회할 댓글이 없음")
//        public void readCommentListThirdTest() {
//
//            // when
//            // then
//            assertThrows(NoMoreCommentException.class, () -> {
//                commentRepository.readNestedCommentList(pageId, 1, 99999L, 10);
//            });
//        }
//    }
//
//
//}