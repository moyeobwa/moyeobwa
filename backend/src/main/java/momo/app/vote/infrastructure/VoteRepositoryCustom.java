package momo.app.vote.infrastructure;

import java.util.List;
import momo.app.gathering.domain.Gathering;
import momo.app.vote.dto.VoteResponse;

public interface VoteRepositoryCustom {
    List<VoteResponse> findAllByGatheringWithCreator(Gathering gathering);
}
