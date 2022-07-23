package cleanbook.com.entity.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Hashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @NotEmpty
    private String name;

    public Hashtag(String name) {
        this.name = name;
    }
}
