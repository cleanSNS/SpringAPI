package cleanbook.com.domain.user;

import cleanbook.com.domain.Timestamped;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Follow extends Timestamped {

    @Id @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private User targetUser;
}
