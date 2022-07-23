package cleanbook.com.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    private String email;

    private String token;
}
