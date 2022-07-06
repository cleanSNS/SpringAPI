package cleanbook.com.service;

import cleanbook.com.domain.page.Comment;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.user.GenderType;
import cleanbook.com.domain.user.User;
import cleanbook.com.domain.user.UserProfile;
import cleanbook.com.domain.user.block.Block;
import cleanbook.com.domain.user.block.BlockedUserDto;
import cleanbook.com.domain.user.like.LikeComment;
import cleanbook.com.domain.user.like.LikePage;
import cleanbook.com.domain.user.like.LikeType;
import cleanbook.com.domain.user.report.ReportComment;
import cleanbook.com.domain.user.report.ReportPage;
import cleanbook.com.domain.user.report.ReportType;
import cleanbook.com.domain.user.report.ReportUser;
import cleanbook.com.repository.CommentRepository;
import cleanbook.com.repository.FollowRepository;
import cleanbook.com.repository.PageRepository;
import cleanbook.com.repository.user.BlockRepository;
import cleanbook.com.repository.user.like.LikeCommentRepository;
import cleanbook.com.repository.user.like.LikePageRepository;
import cleanbook.com.repository.user.UserRepository;

import cleanbook.com.repository.user.report.ReportCommentRepository;
import cleanbook.com.repository.user.report.ReportPageRepository;
import cleanbook.com.repository.user.report.ReportUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PageRepository pageRepository;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private FollowRepository followRepository;
    @Mock
    private ReportUserRepository reportUserRepository;
    @Mock
    private ReportPageRepository reportPageRepository;
    @Mock
    private ReportCommentRepository reportCommentRepository;
    @Mock
    private LikePageRepository likePageRepository;
    @Mock
    private LikeCommentRepository likeCommentRepository;
    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private User user3;
    private User targetUser;
    private Page page;
    private Comment comment;
    private Long sequence = 0L;

    @BeforeEach
    void init() {
        UserProfile userProfile = new UserProfile("a",1, GenderType.FEMALE);
        UserProfile userProfile2 = new UserProfile("b",1, GenderType.FEMALE);
        UserProfile userProfile3 = new UserProfile("c",1, GenderType.FEMALE);
        user = new User(1L,"user", "aaa", userProfile);
        targetUser = new User(2L,"targetUser", "aaa", userProfile2);
        user3 = new User(3L,"user3", "aaa", userProfile3);
        page = new Page(1L, user, "제목", "내용");
        comment = new Comment(1L, user, page, "내용");
    }

    @Test
    @DisplayName("팔로우")
    void followUserTest() {
        //given

        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));

        // when
        userRepository.save(user);
        userRepository.save(targetUser);
        userService.followUser(user.getId(), targetUser.getId());

        // then
        assertThat(user.getFolloweeList().size()).isEqualTo(1);
        assertThat(user.getFolloweeList().get(0).getUser()).isEqualTo(user);
        assertThat(targetUser.getFollowerList().get(0).getTargetUser()).isEqualTo(targetUser);
    }
    
    @Test
    @DisplayName("페이지좋아요")
    void likePageTest() {
    
        //given
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(pageRepository.findById((page.getId()))).willReturn(Optional.of(page));
        given(likePageRepository.save(any(LikePage.class))).willReturn(new LikePage(1L, user, page));

        // when
        Long likeId = userService.like(user.getId(), page.getId(), LikeType.PAGE);
        given(likePageRepository.findById(likeId)).willReturn(Optional.of(new LikePage(1L, user, page)));

        // then
        assertThat(likePageRepository.findById(likeId).get().getUser()).isEqualTo(user);
        assertThat(likePageRepository.findById(likeId).get().getPage()).isEqualTo(page);

    }

    @Test
    @DisplayName("댓글좋아요")
    void likeCommentTest() {

        //given
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(commentRepository.findById((comment.getId()))).willReturn(Optional.of(comment));
        given(likeCommentRepository.save(any(LikeComment.class))).willReturn(new LikeComment(1L, user, comment));

        // when
        Long likeId = userService.like(user.getId(), comment.getId(), LikeType.COMMENT);
        given(likeCommentRepository.findById(likeId)).willReturn(Optional.of(new LikeComment(1L, user, comment)));

        // then
        assertThat(likeCommentRepository.findById(likeId).get().getUser()).isEqualTo(user);
        assertThat(likeCommentRepository.findById(likeId).get().getComment()).isEqualTo(comment);

    }

    @Test
    @DisplayName("유저신고")
    void reportUserTest() {

        //given
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
        given(reportUserRepository.save(any(ReportUser.class))).willReturn(new ReportUser(1L, user, targetUser));

        // when
        Long reportId = userService.report(1L,2L, ReportType.USER);
        given(reportUserRepository.findById(reportId)).willReturn(Optional.of(new ReportUser(1L, user, targetUser)));

        // then
        assertThat(targetUser.getWarningCount()).isEqualTo(1);
        assertThat(reportUserRepository.findById(reportId).get().getUser()).isEqualTo(user);
        assertThat(reportUserRepository.findById(reportId).get().getTargetUser()).isEqualTo(targetUser);
    }

    @Test
    @DisplayName("글신고")
    void reportPageTest() {

        //given
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(pageRepository.findById((page.getId()))).willReturn(Optional.of(page));
        given(reportPageRepository.save(any(ReportPage.class))).willReturn(new ReportPage(1L,user,page));


        // when
        Long reportId = userService.report(1L, 1L, ReportType.PAGE);
        given(reportPageRepository.findById(reportId)).willReturn(Optional.of(new ReportPage(1L, user, page)));

        // then
        assertThat(page.getWarningCount()).isEqualTo(1);
        assertThat(reportPageRepository.findById(reportId).get().getUser()).isEqualTo(user);
        assertThat(reportPageRepository.findById(reportId).get().getTargetPage()).isEqualTo(page);
    }

    @Test
    @DisplayName("댓글신고")
    void reportCommentTest() {

        //given
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(commentRepository.findById((comment.getId()))).willReturn(Optional.of(comment));
        given(reportCommentRepository.save(any(ReportComment.class))).willReturn(new ReportComment(1L,user,comment));


        // when
        Long reportId = userService.report(1L, 1L, ReportType.COMMENT);
        given(reportCommentRepository.findById(reportId)).willReturn(Optional.of(new ReportComment(1L, user, comment)));

        // then
        assertThat(comment.getWarningCount()).isEqualTo(1);
        assertThat(reportCommentRepository.findById(reportId).get().getUser()).isEqualTo(user);
        assertThat(reportCommentRepository.findById(reportId).get().getTargetComment()).isEqualTo(comment);
    }

    @Test
    @DisplayName("차단")
    void blockUser() {

        //given
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
        given(blockRepository.save(any(Block.class))).willReturn(new Block(1L, user, targetUser));

        // when
        Long blockId = userService.blockUser(user.getId(), targetUser.getId());


        // then
        assertThat(user.getBlockUserList().size()).isEqualTo(1);
        assertThat(user.getBlockUserList().get(0).getTargetUser()).isEqualTo(targetUser);

    }

    @Test
    @DisplayName("차단한_유저_전체조회")
    void readBlockedUserList() {

        //given
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
        given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
        given(blockRepository.save(any(Block.class))).willReturn(new Block(++sequence, user, targetUser));

        // when
        userService.blockUser(user.getId(), targetUser.getId());
        userService.blockUser(user.getId(), user3.getId());
        List<BlockedUserDto> blockedUserDtoList = userService.readBlockedUserList(user.getId());


        // then
        assertThat(blockedUserDtoList.size()).isEqualTo(2);
        assertThat(blockedUserDtoList.get(0).getUserId()).isEqualTo(2L);
        assertThat(blockedUserDtoList.get(1).getUserId()).isEqualTo(3L);
        assertThat(blockedUserDtoList.get(0).getNickname()).isEqualTo("b");
        assertThat(blockedUserDtoList.get(1).getNickname()).isEqualTo("c");
    }
    
    @Test
    @DisplayName("차단한_유저_삭제")
    void unblockUserTest() {

        //given
        given(userRepository.findById((user.getId()))).willReturn(Optional.of(user));
        given(userRepository.findById((targetUser.getId()))).willReturn(Optional.of(targetUser));
        given(userRepository.findById((user3.getId()))).willReturn(Optional.of(user3));
        given(blockRepository.save(any(Block.class))).willReturn(new Block(++sequence, user, targetUser));

        // when
        userService.blockUser(user.getId(), targetUser.getId());
        userService.blockUser(user.getId(), user3.getId());
        List<BlockedUserDto> blockedUserDtoList = userService.readBlockedUserList(user.getId());


        // then
        assertThat(blockedUserDtoList.size()).isEqualTo(2);
        assertThat(blockedUserDtoList.get(0).getUserId()).isEqualTo(2L);
        assertThat(blockedUserDtoList.get(1).getUserId()).isEqualTo(3L);
        assertThat(blockedUserDtoList.get(0).getNickname()).isEqualTo("b");
        assertThat(blockedUserDtoList.get(1).getNickname()).isEqualTo("c");

        // when
        userService.unblockUser(user.getId(), targetUser.getId());
        blockedUserDtoList = userService.readBlockedUserList(user.getId());

        // then
        assertThat(blockedUserDtoList.size()).isEqualTo(1);
        assertThat(blockedUserDtoList.get(0).getUserId()).isEqualTo(3L);
        assertThat(blockedUserDtoList.get(0).getNickname()).isEqualTo("c");



    }
}