package cleanbook.com.domain.page;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Getter
public class Hashtag {

    @Id @GeneratedValue
    @Column(name = "hashtag_id")
    private Long id;

    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "page")
    private List<PageHashtag> pageHashtagList;

    void setName(String name) {
        this.name = name;
    }
}
