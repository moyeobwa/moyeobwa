package momo.app.gathering.service;

import lombok.RequiredArgsConstructor;
import momo.app.common.dto.SliceResponse;
import momo.app.gathering.domain.Category;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.gathering.domain.GatheringSortType;
import momo.app.gathering.dto.GatheringResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GatheringQueryService {

    private final GatheringRepository gatheringRepository;

    public SliceResponse<GatheringResponse> findAll(
            String cursor,
            int pageSize,
            GatheringSortType sortType,
            Category category
    ) {

        return gatheringRepository.findAllGatherings(cursor, pageSize, sortType, category);
    }

}
