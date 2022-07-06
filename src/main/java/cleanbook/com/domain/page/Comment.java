package cleanbook.com.domain.page;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @NotEmpty
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotEmpty
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    @NotEmpty
    private String content;

    @Column(name = "orders", columnDefinition = "bigint default 0")
    private int order;

    @Column(name = "groups")
    private int group;

    @Column(columnDefinition = "boolean default true")
    private boolean visible;

    @Column(columnDefinition = "bigint default 0")
    private int warningCount;

    @Column(columnDefinition = "bigint default 0")
    private int likeCount;

    public Comment(Long id, User user, Page page, String content) {
        this.id = id;
        this.user = user;
        this.page = page;
        this.content = content;
    }

    void setUser(User user) {
        this.user = user;
    }

    void setPage(Page page) {
        this.page = page;
        page.getCommentList().add(this);
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
}
