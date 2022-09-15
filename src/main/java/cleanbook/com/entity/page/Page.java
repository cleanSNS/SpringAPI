package cleanbook.com.entity.page;

import cleanbook.com.dto.page.PageCreateDto;
import cleanbook.com.dto.page.PageUpdateDto;
import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static cleanbook.com.entity.page.PageHashtag.createPageHashtag;
import static cleanbook.com.entity.page.PageImgUrl.createPageImgUrl;

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
    @Column(columnDefinition = "TEXT")
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

    public Page(String content) {
        this.content = content;
    }

    public Page(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Page(User user, String content) {
        this.user = user;
        this.content = content;
        user.getPageList().add(this);
    }

    public Page(Long id, User user, String content) {
        this.id = id;
        this.user = user;
        this.content = content;
        user.getPageList().add(this);
    }

    public void reported() {
        this.warningCount++;
    }

    public void likePage() {this.likeCount++;}

    public void unlikePage() {this.likeCount--;}

    public static Page createPage(User user, PageCreateDto pageCreateDto) {
        Page page = new Page();
        page.user = user;
        user.getPageList().add(page);
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
