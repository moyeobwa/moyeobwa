package momo.app.gathering.infrastructure;

public interface GatheringRepositoryCustom {

    boolean existsByGatheringIdAndUserId(Long gatheringId, Long userId);
}
