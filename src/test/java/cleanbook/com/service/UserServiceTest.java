//package cleanbook.com.service;
//
//import cleanbook.com.config.QuerydslConfig;
//import cleanbook.com.dto.ResultDto;
//import cleanbook.com.dto.user.UserDto;
//import cleanbook.com.entity.enums.GenderType;
//import cleanbook.com.entity.page.Comment;
//import cleanbook.com.entity.page.Page;
//import cleanbook.com.entity.user.*;
//import cleanbook.com.entity.user.block.Block;
//import cleanbook.com.dto.user.BlockedUserDto;
//import cleanbook.com.entity.user.follow.Follow;
//import cleanbook.com.entity.user.like.LikeComment;
//import cleanbook.com.entity.user.like.LikePage;
//import cleanbook.com.entity.user.like.LikeType;
//import cleanbook.com.entity.user.report.ReportComment;
//import cleanbook.com.entity.user.report.ReportPage;
//import cleanbook.com.entity.enums.ReportType;
//import cleanbook.com.entity.user.report.ReportUser;
//import cleanbook.com.exception.exceptions.MyException;
//import cleanbook.com.repository.comment.CommentRepository;
//import cleanbook.com.repository.FollowRepository;
//import cleanbook.com.repository.page.PageRepository;
//import cleanbook.com.repository.user.BlockRepository;
//import cleanbook.com.repository.user.like.LikeCommentRepository;
//import cleanbook.com.repository.user.like.LikePageRepository;
//import cleanbook.com.repository.user.UserRepository;
//
//import cleanbook.com.repository.user.report.ReportCommentRepository;
//import cleanbook.com.repository.user.report.ReportPageRepository;
//import cleanbook.com.repository.user.report.ReportUserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Import;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//import java.util.Optional;
//
//import static cleanbook.com.entity.user.like.LikePage.createLikePage;
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@Import(QuerydslConfig.class)
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private PageRepository pageRepository;
//    @Mock
//    private CommentRepository commentRepository;
//
//    @Mock
//    private FollowRepository followRepository;
//    @Mock
//    private ReportUserRepository reportUserRepository;
//    @Mock
//    private ReportPageRepository reportPageRepository;
//    @Mock
//    private ReportCommentRepository reportCommentRepository;
//    @Mock
//    private LikePageRepository likePageRepository;
//    @Mock
//    private LikeCommentRepository likeCommentRepository;
//    @Mock
//    private BlockRepository blockRepository;
//    @Autowired
//    private EntityManager em;
//
//    @InjectMocks
//    private UserService userService;
//
//    private User user;
//    private User user3;
//    private User targetUser;
//    private Page page;
//    private Comment comment;
//    private Long sequence = 0L;
//
//    @BeforeEach
//    void init() {
//        UserProfile userProfile = new UserProfile("a",1, GenderType.FEMALE);
//        UserProfile userProfile2 = new UserProfile("b",1, GenderType.FEMALE);
//        UserProfile userProfile3 = new UserProfile("c",1, GenderType.FEMALE);
//        user = new User(1L,"user", "aaa", userProfile);
//        targetUser = new User(2L,"targetUser", "aaa", userProfile2);
//        user3 = new User(3L,"user3", "aaa", userProfile3);
//        page = new Page(1L, user, "내용");
//        comment = new Comment(1L, user, page, "내용");
//    }
//
//    @Nested
//    @DisplayName("팔로우")
//    class follow{
//
//        @Test
//        @DisplayName("팔로우")
//        void followUserTest() {
//            //given
//
//            given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//            given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
//
//            // when
//            userRepository.save(user);
//            userRepository.save(targetUser);
//            userService.followUser(user.getId(), targetUser.getId());
//
//            // then
//            assertThat(user.getFolloweeList().size()).isEqualTo(1);
//            assertThat(user.getFolloweeList().get(0).getUser()).isEqualTo(user);
//            assertThat(targetUser.getFollowerList().get(0).getTargetUser()).isEqualTo(targetUser);
//        }
//
//        @Test
//        @DisplayName("언팔로우")
//        void unfollowUserTest() {
//            //given
//            given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//            given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
//            userRepository.save(user);
//            userRepository.save(targetUser);
//            Follow follow = userService.followUser(user.getId(), targetUser.getId());
//            given(followRepository.findByUser_IdAndTargetUser_Id(any(Long.class), any(Long.class))).willReturn(Optional.of(follow));
//
//
//            // when
//            userService.unfollowUser(user.getId(), targetUser.getId());
//
//
//            // then
//            assertThat(user.getFolloweeList().size()).isEqualTo(0);
//        }
//    }
//
//
//    @Nested
//    @DisplayName("좋아요")
//    class like{
//
//        @Nested
//        @DisplayName("페이지 좋아요")
//        class likePage{
//
//            @Test
//            @DisplayName("좋아요")
//            void likePageTest() {
//
//                //given
//                given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
//                given(pageRepository.findById((page.getId()))).willReturn(Optional.of(page));
//                given(likePageRepository.findByPage_IdAndUser_Id(any(Long.class), any(Long.class))).willReturn(Optional.empty());
//
//
//                // when
//                userService.like(user3.getId(), page.getId(), LikeType.PAGE);
//
//
//                // then
//                assertThat(page.getLikeCount()).isEqualTo(1);
//            }
//
//            @Test
//            @DisplayName("좋아요 취소")
//            void unlikePageTest() {
//
//                //given
//                given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
//                given(pageRepository.findById((page.getId()))).willReturn(Optional.of(page));
//                LikePage likePage = new LikePage(user3, page);
//
//
//                // when
//                given(likePageRepository.findByPage_IdAndUser_Id(any(Long.class), any(Long.class))).willReturn(Optional.empty());
//                userService.like(user3.getId(), page.getId(), LikeType.PAGE);
//                given(likePageRepository.findByPage_IdAndUser_Id(any(Long.class), any(Long.class))).willReturn(Optional.of(likePage));
//                userService.unlike(user3.getId(), page.getId(), LikeType.PAGE);
//
//
//                // then
//                assertThat(page.getLikeCount()).isEqualTo(0);
//            }
//
//            @Test
//            @DisplayName("잘못된 페이지 좋아요 취소")
//            void unlikePageErrorTest() {
//
//                // given
//                given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
//                given(pageRepository.findById((page.getId()))).willReturn(Optional.of(page));
//
//
//                // when
//                // then
//                MyException exception = assertThrows(MyException.class, () -> {
//                    userService.unlike(user3.getId(), page.getId(), LikeType.PAGE);
//                });
//
//                assertThat(exception.getMessage()).isEqualTo("이미 좋아요 취소가 된 게시글입니다.");
//            }
//
//            @Test
//            @DisplayName("자신의 페이지 좋아요 하기")
//            void likeMyPageTest() {
//
//                //given
//                given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//                given(pageRepository.findById((page.getId()))).willReturn(Optional.of(page));
//
//
//                // when
//                // then
//                MyException exception = assertThrows(MyException.class, () -> {
//                    userService.like(user.getId(), page.getId(), LikeType.PAGE);
//                });
//
//                assertThat(exception.getMessage()).isEqualTo("자신의 게시글에는 좋아요 할 수 없습니다.");
//            }
//        }
//
//        @Nested
//        @DisplayName("댓글 좋아요")
//        class likeComment{
//
//            @Test
//            @DisplayName("좋아요")
//            void likeCommentTest() {
//
//                //given
//                given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
//                given(commentRepository.findById((comment.getId()))).willReturn(Optional.of(comment));
//                given(likeCommentRepository.findByComment_IdAndUser_Id(any(Long.class), any(Long.class))).willReturn(Optional.empty());
//
//
//                // when
//                userService.like(user3.getId(), comment.getId(), LikeType.COMMENT);
//
//
//                // then
//                assertThat(comment.getLikeCount()).isEqualTo(1);
//            }
//
//            @Test
//            @DisplayName("좋아요 취소")
//            void unlikeCommentTest() {
//
//                //given
//                given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
//                given(commentRepository.findById((comment.getId()))).willReturn(Optional.of(comment));
//                LikeComment likeComment = new LikeComment(user3, comment);
//
//
//                // when
//                given(likeCommentRepository.findByComment_IdAndUser_Id(any(Long.class), any(Long.class))).willReturn(Optional.empty());
//                userService.like(user3.getId(), comment.getId(), LikeType.COMMENT);
//                given(likeCommentRepository.findByComment_IdAndUser_Id(any(Long.class), any(Long.class))).willReturn(Optional.of(likeComment));
//                userService.unlike(user3.getId(), comment.getId(), LikeType.COMMENT);
//
//
//                // then
//                assertThat(comment.getLikeCount()).isEqualTo(0);
//            }
//
//            @Test
//            @DisplayName("잘못된 댓글 좋아요 취소")
//            void unlikeCommentErrorTest() {
//
//                // given
//                given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
//                given(commentRepository.findById((comment.getId()))).willReturn(Optional.of(comment));
//
//
//                // when
//                // then
//                MyException exception = assertThrows(MyException.class, () -> {
//                    userService.unlike(user3.getId(), comment.getId(), LikeType.COMMENT);
//                });
//
//                assertThat(exception.getMessage()).isEqualTo("이미 좋아요 취소가 된 댓글입니다.");
//            }
//
//            @Test
//            @DisplayName("자신의 댓글 좋아요 하기")
//            void likeMyCommentTest() {
//
//                //given
//                given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//                given(commentRepository.findById((comment.getId()))).willReturn(Optional.of(comment));
//
//
//                // when
//                // then
//                MyException exception = assertThrows(MyException.class, () -> {
//                    userService.like(user.getId(), comment.getId(), LikeType.COMMENT);
//                });
//
//                assertThat(exception.getMessage()).isEqualTo("자신의 댓글에는 좋아요 할 수 없습니다.");
//            }
//        }
//    }
//
//
//    @Nested
//    @DisplayName("신고")
//    class report {
//
//        @Test
//        @DisplayName("유저신고")
//        void reportUserTest() {
//
//            //given
//            given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//            given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
//            given(reportUserRepository.save(any(ReportUser.class))).willReturn(new ReportUser(1L, user, targetUser));
//
//            // when
//            Long reportId = userService.report(1L,2L, ReportType.USER);
//            given(reportUserRepository.findById(reportId)).willReturn(Optional.of(new ReportUser(1L, user, targetUser)));
//
//            // then
//            assertThat(targetUser.getWarningCount()).isEqualTo(1);
//            assertThat(reportUserRepository.findById(reportId).get().getUser()).isEqualTo(user);
//            assertThat(reportUserRepository.findById(reportId).get().getTargetUser()).isEqualTo(targetUser);
//        }
//
//        @Test
//        @DisplayName("글신고")
//        void reportPageTest() {
//
//            //given
//            given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//            given(pageRepository.findById((page.getId()))).willReturn(Optional.of(page));
//            given(reportPageRepository.save(any(ReportPage.class))).willReturn(new ReportPage(1L,user,page));
//
//
//            // when
//            Long reportId = userService.report(1L, 1L, ReportType.PAGE);
//            given(reportPageRepository.findById(reportId)).willReturn(Optional.of(new ReportPage(1L, user, page)));
//
//            // then
//            assertThat(page.getWarningCount()).isEqualTo(1);
//            assertThat(reportPageRepository.findById(reportId).get().getUser()).isEqualTo(user);
//            assertThat(reportPageRepository.findById(reportId).get().getTargetPage()).isEqualTo(page);
//        }
//
//        @Test
//        @DisplayName("댓글신고")
//        void reportCommentTest() {
//
//            //given
//            given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//            given(commentRepository.findById((comment.getId()))).willReturn(Optional.of(comment));
//            given(reportCommentRepository.save(any(ReportComment.class))).willReturn(new ReportComment(1L,user,comment));
//
//
//            // when
//            Long reportId = userService.report(1L, 1L, ReportType.COMMENT);
//            given(reportCommentRepository.findById(reportId)).willReturn(Optional.of(new ReportComment(1L, user, comment)));
//
//            // then
//            assertThat(comment.getWarningCount()).isEqualTo(1);
//            assertThat(reportCommentRepository.findById(reportId).get().getUser()).isEqualTo(user);
//            assertThat(reportCommentRepository.findById(reportId).get().getTargetComment()).isEqualTo(comment);
//        }
//
//        @Test
//        @DisplayName("중복 신고")
//        void duplicateReport() {
//
//            given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//            given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
//            given(reportUserRepository.save(any(ReportUser.class))).willReturn(new ReportUser(1L, user, targetUser));
//            userService.report(1L,2L, ReportType.USER);
//            given(reportUserRepository.findByUser_IdAndTargetUser_Id(user.getId(), targetUser.getId())).willReturn(Optional.of(new ReportUser()));
//
//            // when
//            // then
//
//            MyException exception = assertThrows(MyException.class, () -> {
//                userService.report(1L, 2L, ReportType.USER);
//            });
//
//            assertThat(exception.getMessage()).isEqualTo("이미 신고한 유저입니다.");
//        }
//    }
//
//
//
//
//    @Test
//    @DisplayName("차단")
//    void blockUser() {
//
//        //given
//        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
//        given(blockRepository.save(any(Block.class))).willReturn(new Block(1L, user, targetUser));
//
//        // when
//        userService.blockUser(user.getId(), targetUser.getId());
//
//
//        // then
//        assertThat(user.getBlockUserList().size()).isEqualTo(1);
//        assertThat(user.getBlockUserList().get(0).getTargetUser()).isEqualTo(targetUser);
//
//    }
//
//    @Test
//    @DisplayName("차단 해제")
//    void unblockUserTest() {
//
//        //given
//        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
//        Block block = new Block(user, targetUser);
//
//        userService.blockUser(user.getId(), targetUser.getId());
//        given(blockRepository.findByUser_IdAndTargetUser_Id(any(Long.class), any(Long.class))).willReturn(Optional.of(block));
//
//
//        // when
//        userService.unblockUser(user.getId(), targetUser.getId());
//
//
//        // then
//        assertThat(user.getBlockUserList().size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("차단한_유저_전체조회")
//    void readBlockedUserList() {
//
//        //given
//        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
//        given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
//        given(blockRepository.save(any(Block.class))).willReturn(new Block(++sequence, user, targetUser));
//
//        // when
//        userService.blockUser(user.getId(), targetUser.getId());
//        userService.blockUser(user.getId(), user3.getId());
//        ResultDto<List<BlockedUserDto>> resultDto = userService.readBlockedUserList(user.getId());
//        List<BlockedUserDto> blockedUserDtoList = resultDto.getData();
//
//
//        // then
//        assertThat(blockedUserDtoList.size()).isEqualTo(2);
//        assertThat(blockedUserDtoList.get(0).getUserId()).isEqualTo(2L);
//        assertThat(blockedUserDtoList.get(1).getUserId()).isEqualTo(3L);
//        assertThat(blockedUserDtoList.get(0).getNickname()).isEqualTo("b");
//        assertThat(blockedUserDtoList.get(1).getNickname()).isEqualTo("c");
//    }
//
//
//
//    @Test
//    @DisplayName("내가 팔로우한 유저 전체 조회")
//    void readFolloweeListTest() {
//
//        //given
//        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
//        given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
//        userRepository.save(user);
//        userRepository.save(targetUser);
//        userRepository.save(user3);
//        userService.followUser(user.getId(), targetUser.getId());
//        userService.followUser(user.getId(), user3.getId());
//
//
//        // when
//        ResultDto<List<UserDto>> resultDto = userService.readFolloweeList(user.getId());
//        List<UserDto> userDtoList = resultDto.getData();
//
//
//        // then
//        assertThat(userDtoList.size()).isEqualTo(2);
//        assertThat(userDtoList).extracting("nickname").containsExactly("b", "c");
//
//    }
//
//    @Test
//    @DisplayName("나를 팔로우한 유저 전체 조회")
//    void readFollowerListTest() {
//
//        //given
//        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
//        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
//        given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
//        userRepository.save(user);
//        userRepository.save(targetUser);
//        userRepository.save(user3);
//        userService.followUser(targetUser.getId(), user.getId());
//        userService.followUser(user3.getId(), user.getId());
//
//
//        // when
//        ResultDto<List<UserDto>> resultDto = userService.readFollowerList(user.getId());
//        List<UserDto> userDtoList = resultDto.getData();
//
//
//        // then
//        assertThat(userDtoList.size()).isEqualTo(2);
//        assertThat(userDtoList).extracting("nickname").containsExactly("b", "c");
//
//    }
//}