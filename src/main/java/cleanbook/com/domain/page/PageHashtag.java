package cleanbook.com.domain.page;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class PageHashtag {

    @Id @GeneratedValue
    @Column(name = "page_hashtag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    void setPage(Page page) {
        this.page = page;
        page.getPageHashtagList().add(this);
    }

    void setHashtag(Hashtag hashtag) {
        this.hashtag = hashtag;
        hashtag.getPageHashtagList().add(this);
    }
}
