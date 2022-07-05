package cleanbook.com.domain.user.like;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.page.Comment;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeComment extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "like_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Comment")
    private Comment comment;

    public LikeComment(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

//    void setUser(User user) {
//        this.user = user;
//    }
//
//    void setComment(Comment comment) {
//        this.comment = comment;
//    }
}
