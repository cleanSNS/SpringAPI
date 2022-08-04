package cleanbook.com.entity.page;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class PageHashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_hashtag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    public static PageHashtag createPageHashtag(Page page, Hashtag hashtag) {
        PageHashtag pageHashtag = new PageHashtag();
        pageHashtag.page = page;
        pageHashtag.hashtag = hashtag;
        page.getPageHashtagList().add(pageHashtag);
        return pageHashtag;
    }
}
