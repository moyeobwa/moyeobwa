package momo.app.gathering.infrastructure;

import static momo.app.gathering.domain.QGathering.gathering;
import static momo.app.gathering.domain.QGatheringMember.gatheringMember;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import momo.app.gathering.domain.GatheringMember;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class GatheringRepositoryImpl implements GatheringRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean checkExistsByGatheringIdAndUserId(Long gatheringId, Long userId) {
        GatheringMember result = jpaQueryFactory.select(gatheringMember)
                .from(gathering)
                .innerJoin(gathering.gatheringMembers, gatheringMember)
                .where(gatheringMember.gathering.id.eq(gatheringId).and(gatheringMember.user.id.eq(userId)))
                .fetchFirst();

        return result != null;
    }
}
