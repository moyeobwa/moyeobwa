package momo.app.gathering.infrastructure;

import momo.app.common.dto.SliceResponse;
import momo.app.gathering.domain.Category;
import momo.app.gathering.domain.GatheringSortType;
import momo.app.gathering.dto.GatheringResponse;

public interface GatheringRepositoryCustom {

    boolean checkExistsByGatheringIdAndUserId(Long gatheringId, Long userId);

    SliceResponse<GatheringResponse> findAllGatherings(
            String cursor, int pageSize, GatheringSortType sortType, Category category);
}
