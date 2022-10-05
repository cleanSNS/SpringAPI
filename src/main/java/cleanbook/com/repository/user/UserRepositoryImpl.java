package cleanbook.com.repository.user;

import cleanbook.com.dto.ResultDto;
import cleanbook.com.dto.user.UserDto;
import cleanbook.com.entity.user.block.QBlock;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cleanbook.com.entity.user.QUser.*;
import static cleanbook.com.entity.user.block.QBlock.block;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    // 차단 했거나 당했으면 검색 불가
    public ResultDto<List<UserDto>> findUsersStartWithNickname(Long userId, String nickname) {
        QBlock block2 = new QBlock("block2");

        List<UserDto> result = jpaQueryFactory.query()
                .select(Projections.constructor(UserDto.class,
                        user.id,
                        user.userProfile.nickname,
                        user.userProfile.imgUrl))
                .from(user)
                .leftJoin(user.blockedUserList, block)
                .leftJoin(user.blockUserList, block2)
                .where(user.userProfile.nickname.startsWith(nickname)
                        , exceptBlockedUser(userId)
                        , exceptBlockUser(userId, block2)
                )
                .fetch();

        return new ResultDto<>(result);
    }

    // 차단한 경우
    // 차단한 사람이 없을 경우를 생각해 null을 넣어줘야함
    BooleanExpression exceptBlockedUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return block.isNull().or(block.targetUser.id.ne(userId));
    }

    // 차단당한 경우
    BooleanExpression exceptBlockUser(Long userId, QBlock block2) {
        if (userId == null) {
            return null;
        }
        return block2.isNull().or(block2.user.id.ne(userId));
    }
}
