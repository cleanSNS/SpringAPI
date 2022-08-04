package cleanbook.com.repository.comment;

import cleanbook.com.config.QuerydslConfig;
import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.page.CommentDto;
import cleanbook.com.entity.enums.GenderType;
import cleanbook.com.entity.page.Comment;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.UserProfile;
import cleanbook.com.repository.page.PageRepository;
import cleanbook.com.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class CommentRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void init() {
        int group = 0;
        for (int i = 0; i < 2; i++) {
            UserProfile userProfile = new UserProfile(Integer.toString(i), i, GenderType.FEMALE);
            User user = new User("aa", "aa", userProfile);
            userRepository.save(user);

            Page page = new Page(user, "글내용");
            pageRepository.save(page);

            for (int j = 0; j < 15; j++) {
                Comment comment = Comment.builder()
                        .user(user)
                        .page(page)
                        .visible(true)
                        .content("내용")
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
                            .content("내용")
                            .nested(true)
                            .group(group)
                            .order(order++)
                            .build();
                    commentRepository.save(nestedComment);
                }
            }
        }
    }

    @Test
    public void readCommentListTest() {

        // when
        ResultDto<List<CommentDto>> resultDto = commentRepository.readCommentList(1L, 1L, 10);
        List<CommentDto> commentDtoList = resultDto.getData();
        Long startPageId = resultDto.getStartId();

        // then
        assertThat(commentDtoList).extracting("commentId").contains(1L,7L,13L);
        assertThat(startPageId).isEqualTo(56);

        resultDto = commentRepository.readCommentList(1L, startPageId, 10);
        commentDtoList = resultDto.getData();
        startPageId = resultDto.getStartId();

        for (CommentDto commentDto : commentDtoList) {
            System.out.println("commentDto = " + commentDto);
        }

        assertThat(commentDtoList.size()).isEqualTo(5);
        assertThat(commentDtoList).extracting("commentId").contains(61L,67L);

    }
    
    @Test
    @DisplayName("대댓글_조회")
    void readNestedCommentListTest() {
    
        // when
        ResultDto<List<CommentDto>> resultDto = commentRepository.readNestedCommentList(1L, 1, 1L, 10);
        List<CommentDto> commentDtoList = resultDto.getData();
        Long startPageId = resultDto.getStartId();

        for (CommentDto commentDto : commentDtoList) {
            System.out.println("commentDto = " + commentDto);
        }
        
        // then
        assertThat(commentDtoList).extracting("commentId").containsExactly(2L,3L,4L,5L,6L);
        assertThat(startPageId).isEqualTo(7L);
    }
}