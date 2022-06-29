package cleanbook.com.domain.page;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
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

    private String title;
    private String content;
    private String imgUrl;
    private int warningCount;

    @Embedded
    private PageSetting pageSetting;

    @OneToMany(mappedBy = "page")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "page")
    private List<PageHashtag> pageHashtagList;
}
