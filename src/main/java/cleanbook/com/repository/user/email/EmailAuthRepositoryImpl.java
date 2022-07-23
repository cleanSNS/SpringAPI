package cleanbook.com.repository.user.email;

import cleanbook.com.entity.user.EmailAuth;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static cleanbook.com.entity.user.QEmailAuth.emailAuth;

@RequiredArgsConstructor
public class EmailAuthRepositoryImpl implements EmailAuthRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime currentTime) {
        EmailAuth findEmailAuth = jpaQueryFactory
                .selectFrom(emailAuth)
                .where(emailAuth.email.eq(email),
                        emailAuth.authToken.eq(authToken),
                        emailAuth.expireDate.goe(currentTime),
                        emailAuth.expired.eq(false))
                .fetchFirst();

        return Optional.ofNullable(findEmailAuth);
    }
}
