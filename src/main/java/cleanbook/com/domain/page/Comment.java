package cleanbook.com.domain.page;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.user.User;
import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Comment extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    @NotNull
    private String content;

    @Column(columnDefinition = "integer default 0")
    private int order;
    private int group;

    @Column(columnDefinition = "boolean default true")
    private boolean visible;

    @Column(columnDefinition = "integer default 0")
    private int warningCount;
}
