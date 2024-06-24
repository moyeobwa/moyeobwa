package momo.app.vote.infrastructure;

import static momo.app.user.domain.QUser.user;
import static momo.app.vote.domain.QVote.vote;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import momo.app.gathering.domain.Gathering;
import momo.app.vote.dto.VoteResponse;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VoteRepositoryImpl implements VoteRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<VoteResponse> findAllByGatheringWithCreator(Gathering gathering) {
        return jpaQueryFactory.select(Projections.constructor(
                VoteResponse.class,
                vote.title,
                vote.id,
                vote.createdDate,
                user.name))
                .from(vote)
                .join(user).on(vote.creatorId.eq(user.id))
                .where(vote.gathering.eq(gathering))
                .orderBy(vote.id.desc())
                .fetch();
    }
}
