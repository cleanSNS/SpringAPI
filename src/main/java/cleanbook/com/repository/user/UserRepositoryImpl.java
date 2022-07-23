package cleanbook.com.repository.user;

import cleanbook.com.dto.user.UserDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cleanbook.com.entity.user.QUser.*;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserDto> findUsersStartWithNickname(String nickname) {
        List<UserDto> result = jpaQueryFactory.query()
                .select(Projections.constructor(UserDto.class,
                        user.id,
                        user.userProfile.nickname,
                        user.userProfile.imgUrl))
                .from(user)
                .where(user.userProfile.nickname.startsWith(nickname))
                .fetch();

        return result;
    }
}
