package cleanbook.com.domain.page;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.user.User;
import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
public class Comment extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @NotEmpty
    @OneToOne(fetch = FetchType.LAZY)
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
}
