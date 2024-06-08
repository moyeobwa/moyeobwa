package momo.app.vote.service;

import static momo.app.gathering.exception.GatheringErrorCode.USER_NOT_IN_GATHERING;
import static momo.app.vote.exception.VoteErrorCode.VOTE_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.common.error.exception.BusinessException;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.gathering.exception.GatheringErrorCode;
import momo.app.vote.domain.Option;
import momo.app.vote.domain.OptionRepository;
import momo.app.vote.domain.Vote;
import momo.app.vote.domain.VoteRepository;
import momo.app.vote.dto.OptionResponse;
import momo.app.vote.exception.VoteErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptionQueryService {

    private final OptionRepository optionRepository;
    private final VoteRepository voteRepository;
    private final GatheringRepository gatheringRepository;

    public List<OptionResponse> findAll(AuthUser authUser, Long voteId) {
        Vote vote = findVote(voteId);
        validateUserInGathering(authUser, vote);
        List<Option> options = optionRepository.findByVote(vote);

        return options.stream()
                .map(OptionResponse::from)
                .toList();
    }

    private void validateUserInGathering(AuthUser authUser, Vote vote) {
        if (!gatheringRepository.existsByGatheringIdAndUserId(vote.getGathering().getId(), authUser.getId())) {
            throw new BusinessException(USER_NOT_IN_GATHERING);
        }
    }

    private Vote findVote(Long voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new BusinessException(VOTE_NOT_FOUND));
    }

}
