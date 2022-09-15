//package cleanbook.com.service;
//
//import cleanbook.com.dto.page.CommentCreateDto;
//import cleanbook.com.entity.enums.GenderType;
//import cleanbook.com.entity.page.Comment;
//import cleanbook.com.entity.page.Page;
//import cleanbook.com.entity.user.User;
//import cleanbook.com.entity.user.UserProfile;
//import cleanbook.com.exception.exceptions.CommentNotFoundException;
//import cleanbook.com.repository.comment.CommentRepository;
//import cleanbook.com.repository.page.PageRepository;
//import cleanbook.com.repository.user.UserRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//class CommentServiceTest {
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PageRepository pageRepository;
//    @Autowired
//    private CommentService commentService;
//    @Autowired
//    private CommentRepository commentRepository;
//    @Autowired
//    private EntityManager em;
//
//    @BeforeEach
//    void init() {
//        int group = 0;
//        for (int i = 0; i < 2; i++) {
//            UserProfile userProfile = new UserProfile(Integer.toString(i), i, GenderType.FEMALE);
//            User user = new User("aa", "aa", userProfile);
//            userRepository.save(user);
//
//            for (int j = 0; j < 2; j++) {
//                Page page = new Page(user, Integer.toString(j));
//                pageRepository.save(page);
//                CommentCreateDto comment = CommentCreateDto.builder()
//                        .pageId(page.getId())
//                        .visible(true)
//                        .content("내용")
//                        .nested(false)
//                        .group(++group)
//                        .build();
//                commentService.createComment(user.getId(), comment);
//
//                for (int k = 0; k < 5; k++) {
//                    CommentCreateDto reply = CommentCreateDto.builder()
//                            .pageId(page.getId())
//                            .visible(true)
//                            .content("내용")
//                            .nested(true)
//                            .group(group)
//                            .build();
//                    commentService.createComment(user.getId(), reply);
//                }
//            }
//        }
//    }
//
//}