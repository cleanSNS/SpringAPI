package cleanbook.com.domain.page;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class PageImgUrl {

    @Id @GeneratedValue
    @Column(name = "page_img_url_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    private String url;

    void setPage(Page page) {
        this.page = page;
        page.getPageImgUrlList().add(this);
    }

    void setUrl(String url) {
        this.url = url;
    }
}
