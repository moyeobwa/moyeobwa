package momo.app.gathering.infrastructure;

import momo.app.common.dto.SliceResponse;
import momo.app.gathering.domain.Category;
import momo.app.gathering.domain.GatheringSortType;
import momo.app.gathering.dto.GatheringResponse;
import momo.app.gathering.dto.GatheringNameResponse;
import momo.app.user.domain.User;

import java.util.List;

public interface GatheringRepositoryCustom {

    boolean checkExistsByGatheringIdAndUserId(Long gatheringId, Long userId);

    SliceResponse<GatheringResponse> findAllGatherings(
            String cursor, int pageSize, GatheringSortType sortType, Category category);

    List<GatheringNameResponse> findAllGatheringsByUser(User user);
}
