package cleanbook.com.domain.user.like;

import cleanbook.com.domain.Timestamped;
import cleanbook.com.domain.page.Page;
import cleanbook.com.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikePage extends Timestamped {

    @Id @GeneratedValue
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

    void setUser(User user) {
        this.user = user;
    }

    void setPage(Page page) {
        this.page = page;
    }
}
