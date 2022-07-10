package cleanbook.com.domain.page;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static cleanbook.com.domain.page.PageHashtag.createPageHashtag;
import static cleanbook.com.domain.page.PageImgUrl.createPageImgUrl;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Page extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
    private List<PageImgUrl> imgUrlList = new ArrayList<>();

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<PageHashtag> pageHashtagList = new ArrayList<>();

    public Page(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Page(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Page(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        user.getPageList().add(this);
    }

    public Page(Long id, User user, String title, String content) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        user.getPageList().add(this);
    }

    public void reported() {
        this.warningCount++;
    }

    public static Page createPage(User user, PageCreateDto pageCreateDto) {
        Page page = new Page();
        page.user = user;
        user.getPageList().add(page);
        page.title = pageCreateDto.getTitle();
        page.content = pageCreateDto.getContent();
        page.pageSetting = pageCreateDto.getPageSetting();
        page.imgUrlList.clear();
        page.pageHashtagList.clear();
        for (String imgUrl : pageCreateDto.getImgUrlList()) {
            createPageImgUrl(page,imgUrl);
        }
        for (String name : pageCreateDto.getPageHashtagList()) {
            Hashtag hashtag = new Hashtag(name);
            createPageHashtag(page, hashtag);
        }
        return page;
    }

    public void updatePage(PageUpdateDto pageUpdateDto) {
        this.title = pageUpdateDto.getTitle();
        this.content = pageUpdateDto.getContent();
        this.pageSetting = pageUpdateDto.getPageSetting();
        this.imgUrlList.clear();
        this.pageHashtagList.clear();
        for (String imgUrl : pageUpdateDto.getImgUrlList()) {
            createPageImgUrl(this,imgUrl);
        }
        for (String name : pageUpdateDto.getPageHashtagList()) {
            Hashtag hashtag = new Hashtag(name);
            createPageHashtag(this, hashtag);
        }
    }
}
