package cleanbook.com.entity.user.like;

import cleanbook.com.entity.Timestamped;
import cleanbook.com.entity.page.Page;
import cleanbook.com.entity.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LikePage extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_page_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    public LikePage(User user, Page page) {
        this.user = user;
        this.page = page;
    }

    public static LikePage createLikePage(User user, Page page) {
        LikePage likePage = new LikePage();
        likePage.user = user;
        likePage.page = page;
        page.likePage();
        return likePage;
    }
}
