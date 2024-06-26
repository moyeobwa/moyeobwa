package momo.app.gathering.infrastructure;

import static momo.app.gathering.domain.GatheringSortType.LATEST;
import static momo.app.gathering.domain.QGathering.gathering;
import static momo.app.gathering.domain.QGatheringMember.gatheringMember;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import momo.app.common.dto.SliceResponse;
import momo.app.gathering.domain.Category;
import momo.app.gathering.domain.GatheringMember;
import momo.app.gathering.domain.GatheringSortType;
import momo.app.gathering.dto.GatheringResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class GatheringRepositoryImpl implements GatheringRepositoryCustom {

    private static final int MAX_MEMBER_COUNT_DIGIT = 6;

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

    @Override
    public SliceResponse<GatheringResponse> findAllGatherings(
            String cursor,
            int pageSize,
            GatheringSortType sortType,
            Category category
    ) {
        List<GatheringResponse> result = jpaQueryFactory.select(Projections.constructor(
                        GatheringResponse.class,
                        gathering.id,
                        gathering.gatheringInfo.name,
                        gathering.gatheringInfo.description,
                        gathering.createdDate,
                        gathering.gatheringInfo.imageUrl,
                        gatheringMember.countDistinct().intValue()
                ))
                .from(gathering)
                .leftJoin(gatheringMember).on(gatheringMember.gathering.id.eq(gathering.id))
                .where(categoryEq(category))
                .groupBy(gathering)
                .having(isInSearchRange(cursor, sortType))
                .orderBy(createGatheringSpecifier(sortType), gathering.id.desc())
                .limit(pageSize + 1)
                .fetch();

        return convertToSlice(result, sortType, pageSize);
    }

    private BooleanExpression categoryEq(Category category) {
        if (category != null) {
            return gathering.gatheringInfo.category.eq(category);
        }

        return null;
    }

    private BooleanExpression isInSearchRange(String cursor, GatheringSortType sortType) {
        if (cursor == null) {
            return null;
        }

        if (sortType == LATEST) {
            return gathering.id.lt(Long.valueOf(cursor));
        }

        int memberCount = Integer.parseInt(cursor.substring(0, MAX_MEMBER_COUNT_DIGIT));
        long gatheringId = Long.parseLong(cursor.substring(MAX_MEMBER_COUNT_DIGIT));

        return gathering.count().lt(memberCount)
                .or(gathering.count().intValue().eq(memberCount).and(gathering.id.lt(gatheringId)));
    }

    private SliceResponse<GatheringResponse> convertToSlice(
            List<GatheringResponse> result,
            GatheringSortType sortType,
            int pageSize
    ) {
        if (result.isEmpty()) {
            return SliceResponse.of(result, false, null);
        }

        boolean hasNext = existNextPage(result, pageSize);
        if (existNextPage(result, pageSize)) {
            deleteLastPage(result, pageSize);
        }

        String nextCursor = generateCursor(result, sortType);
        return SliceResponse.of(result, hasNext, nextCursor);
    }

    private boolean existNextPage(List<GatheringResponse> result, int pageSize) {
        return result.size() > pageSize;
    }

    private void deleteLastPage(List<GatheringResponse> result, int pageSize) {
        result.remove(pageSize);
    }

    private String generateCursor(List<GatheringResponse> result, GatheringSortType sortType) {
        GatheringResponse lastGathering = result.get(result.size() - 1);

        return switch (sortType) {
            case MEMBER_COUNT -> String.format("%06d", lastGathering.numberOfMembers())
                    + String.format("%08d", lastGathering.id());
            default -> String.valueOf(lastGathering.id());
        };
    }

    private OrderSpecifier createGatheringSpecifier(GatheringSortType sortType) {
        return switch (sortType) {
            case MEMBER_COUNT -> new OrderSpecifier<>(Order.DESC, gatheringMember.countDistinct());
            default -> new OrderSpecifier<>(Order.DESC, gathering.id);
        };
    }
}
