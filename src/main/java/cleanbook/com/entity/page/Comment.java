package cleanbook.com.entity.page;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    @NotEmpty
    private String content;

    @Builder.Default
    private boolean nested = false;

    @Builder.Default
    @Column(name = "orders")
    private int order = 0;

    @Column(name = "comment_group")
    private int group;

    @Builder.Default
    private boolean visible = true;

    @Builder.Default
    private int warningCount = 0;

    @Builder.Default
    private int likeCount = 0;

    public static Comment createComment(User user, Page page, String content, int group, boolean nested, boolean visible) {
        Comment comment = Comment.builder()
                .user(user)
                .page(page)
                .content(content)
                .group(group)
                .nested(nested)
                .visible(visible)
                .build();
        page.getCommentList().add(comment);

        return comment;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Comment(User user, Page page, String content) {
        this.user = user;
        this.page = page;
        this.content = content;
        page.getCommentList().add(this);
    }

    public Comment(Long id, User user, Page page, String content) {
        this.id = id;
        this.user = user;
        this.page = page;
        this.content = content;
    }

    void setCommentContents(String content, int order, int group, boolean visible) {
        this.content = content;
        this.order = order;
        this.group = group;
        this.visible = visible;
    }

    public void reported() {
        this.warningCount++;
    }

    public void likeComment() {this.likeCount++;}

    public void unlikeComment() {this.likeCount--;}

}
