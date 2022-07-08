package cleanbook.com.domain.page;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "page_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<PageImgUrl> imgUrlList = new ArrayList<>();

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @Column(columnDefinition = "bigint default 0")
    private int warningCount;

    @Column(columnDefinition = "bigint default 0")
    private int likeCount;

    @Embedded
    private PageSetting pageSetting;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "page")
    private List<PageHashtag> pageHashtagList = new ArrayList<>();

    void setUser(User user) {
        this.user = user;
        user.getPageList().add(this);
    }

    void setTitleAndContent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Page(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public Page(Long id, User user, String title, String content) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public void reported() {
        this.warningCount++;
    }
}
