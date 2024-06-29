package momo.app.vote.infrastructure;

import static momo.app.vote.domain.QOption.option;
import static momo.app.vote.domain.QUserOption.userOption;
import static momo.app.vote.domain.QVote.vote;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import momo.app.user.domain.User;
import momo.app.vote.domain.QOption;
import momo.app.vote.domain.QVote;
import momo.app.vote.domain.UserOption;
import momo.app.vote.domain.Vote;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserOptionRepositoryImpl implements UserOptionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean checkByVoteAndUser(Vote vote, User user) {
        UserOption result = jpaQueryFactory.selectFrom(userOption)
                .join(userOption.option, option)
                .join(option.vote)
                .where(QVote.vote.eq(vote).and(
                        userOption.user.id.eq(user.getId())))
                .fetchFirst();

        return result != null;
    }
}
