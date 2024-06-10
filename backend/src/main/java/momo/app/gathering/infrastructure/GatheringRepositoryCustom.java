package momo.app.gathering.infrastructure;

public interface GatheringRepositoryCustom {

    boolean checkExistsByGatheringIdAndUserId(Long gatheringId, Long userId);
}
