package cleanbook.com.entity.user.authority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Authority {

    @Id
    @Column(name = "authority_name")
    private String name;
}
