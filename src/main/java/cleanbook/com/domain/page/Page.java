package cleanbook.com.domain.page;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Getter
public class Page extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "page_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private int warningCount;

    @Embedded
    private PageSetting pageSetting;

    @OneToMany(mappedBy = "page")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "page")
    private List<PageHashtag> pageHashtagList;

    @OneToMany(mappedBy = "page")
    private List<PageImgUrl> pageImgUrlList;

    void setUser(User user) {
        this.user = user;
        user.getPageList().add(this);
    }

    void setTitleAndContent(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
