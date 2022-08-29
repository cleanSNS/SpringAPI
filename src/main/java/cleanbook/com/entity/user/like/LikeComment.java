package cleanbook.com.entity.user.like;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.page.Comment;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LikeComment extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public LikeComment(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

    public static LikeComment createLikeComment(User user, Comment comment) {
        LikeComment likeComment = new LikeComment();
        likeComment.user = user;
        likeComment.comment = comment;
        comment.likeComment();
        return likeComment;
    }

}
