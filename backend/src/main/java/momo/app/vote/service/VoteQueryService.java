package momo.app.vote.service;

import static momo.app.gathering.exception.GatheringErrorCode.GATHERING_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.common.error.exception.BusinessException;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.gathering.exception.GatheringErrorCode;
import momo.app.vote.domain.Vote;
import momo.app.vote.domain.VoteRepository;
import momo.app.vote.dto.VoteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteQueryService {

    private final VoteRepository voteRepository;
    private final GatheringRepository gatheringRepository;

    public List<VoteResponse> findAll(AuthUser authUser, Long gatheringId) {
        Gathering gathering = findGathering(gatheringId);
        validateUserInGathering(authUser, gatheringId);
        List<Vote> votes = voteRepository.findByGathering(gathering);
        return votes.stream()
                .map(vote -> VoteResponse.from(vote))
                .toList();
    }

    private void validateUserInGathering(AuthUser authUser, Long gatheringId) {
        if (!gatheringRepository.existsByGatheringIdAndUserId(gatheringId, authUser.getId())) {
            throw new BusinessException(GatheringErrorCode.USER_NOT_IN_GATHERING);
        }
    }

    private Gathering findGathering(Long gatheringId) {
        return gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new BusinessException(GATHERING_NOT_FOUND));
    }
}
