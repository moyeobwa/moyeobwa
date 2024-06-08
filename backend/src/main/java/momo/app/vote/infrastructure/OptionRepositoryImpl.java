package momo.app.vote.infrastructure;

import static momo.app.vote.domain.QOption.option;
import static momo.app.vote.domain.QVote.vote;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import momo.app.vote.domain.Option;

@RequiredArgsConstructor
public class OptionRepositoryImpl implements OptionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByIdAndVoteId(Long id, Long voteId) {
        Option result = jpaQueryFactory.selectFrom(option)
                .innerJoin(option.vote, vote)
                .where(option.id.eq(id).and(vote.id.eq(voteId)))
                .fetchFirst();

        return result != null;
    }
}
