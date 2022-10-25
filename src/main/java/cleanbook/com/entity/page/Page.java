package cleanbook.com.entity.page;

import cleanbook.com.dto.page.PageCreateDto;
import cleanbook.com.dto.page.PageUpdateDto;
import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.user.User;
import cleanbook.com.entity.user.like.LikePage;
import lombok.*;

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
@AllArgsConstructor
@Builder
public class Page extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String filteredContent;

    @Column(columnDefinition = "bigint default 0")
    private int warningCount;

    @Column(columnDefinition = "bigint default 0")
    private int likeCount;

    @Embedded
    @Builder.Default
    private PageSetting pageSetting = PageSetting.builder().build();

    @Builder.Default
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<PageImgUrl> pageImgUrlList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<PageHashtag> pageHashtagList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<LikePage> likePageList = new ArrayList<>();

    public Page(String content) {
        this.content = content;
    }

    public Page(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    @Builder
    public Page(User user, String content) {
        this.user = user;
        this.content = content;
        user.getPageList().add(this);
    }

    @Builder
    public Page(Long id, User user, String content) {
        this.id = id;
        this.user = user;
        this.content = content;
        user.getPageList().add(this);
    }

    public Page(Long id, User user, String content, PageSetting pageSetting) {
        this.id = id;
        this.user = user;
        this.content = content;
        user.getPageList().add(this);
        this.pageSetting = pageSetting;
    }

    public void reported() {
        this.warningCount++;
    }

    public void likePage() {this.likeCount++;}

    public void unlikePage() {this.likeCount--;}

    public static Page createPage(User user, PageCreateDto pageCreateDto) {
        Page page = Page.builder()
                .user(user)
                .content(pageCreateDto.getContent())
                .pageSetting(pageCreateDto.getPageSetting())
                .build();

        for (String imgUrl : pageCreateDto.getImgUrlList()) {
            createPageImgUrl(page,imgUrl);
        }

        for (String name : pageCreateDto.getPageHashtagList()) {
            Hashtag hashtag = new Hashtag(name);
            createPageHashtag(page, hashtag);
        }
        user.getPageList().add(page);

        return page;
    }

    public void updatePage(PageUpdateDto pageUpdateDto) {
        this.content = pageUpdateDto.getContent();
        this.pageSetting = pageUpdateDto.getPageSetting();
        this.pageImgUrlList.clear();
        this.pageHashtagList.clear();
        for (String imgUrl : pageUpdateDto.getImgUrlList()) {
            createPageImgUrl(this,imgUrl);
        }
        for (String name : pageUpdateDto.getPageHashtagList()) {
            Hashtag hashtag = new Hashtag(name);
            createPageHashtag(this, hashtag);
        }
    }

    public void updateFilteredContent(String filteredContent) {
        this.filteredContent = filteredContent;
    }
}
