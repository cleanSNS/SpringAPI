package cleanbook.com.entity.page;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class PageImgUrl {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_img_url_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    public static PageImgUrl createPageImgUrl(Page page, String url) {
        PageImgUrl pageImgUrl = new PageImgUrl();
        pageImgUrl.page = page;
        pageImgUrl.imgUrl = url;
        page.getImgUrlList().add(pageImgUrl);

        return pageImgUrl;
    }
}
