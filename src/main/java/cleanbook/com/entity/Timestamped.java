package cleanbook.com.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass // 멤버 변수가 컬럼이 되도록 함.
@EntityListeners(AuditingEntityListener.class) // 변경되었을 때 자동으로 기록.
public abstract class Timestamped {

    @CreatedDate // 최초 생성 시점
    private LocalDateTime createdDate;
}