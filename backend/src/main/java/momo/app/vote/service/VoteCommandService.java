package momo.app.vote.service;

import static momo.app.gathering.exception.GatheringErrorCode.GATHERING_NOT_FOUND;
import static momo.app.gathering.exception.GatheringErrorCode.USER_NOT_IN_GATHERING;
import static momo.app.vote.exception.OptionErrorCode.OPTION_NOT_FOUND;
import static momo.app.vote.exception.OptionErrorCode.TOO_FEW_OPTIONS;
import static momo.app.vote.exception.VoteErrorCode.ALREADY_VOTE_END;
import static momo.app.vote.exception.VoteErrorCode.VOTE_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.common.error.exception.BusinessException;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.vote.domain.Option;
import momo.app.vote.domain.OptionRepository;
import momo.app.vote.domain.Vote;
import momo.app.vote.domain.VoteRepository;
import momo.app.vote.dto.VoteCreateRequest;
import momo.app.vote.dto.VoteRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteCommandService {

    private final static int MIN_NUMBER_OF_OPTIONS = 2;

    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;
    private final GatheringRepository gatheringRepository;

    public Long create(VoteCreateRequest request, AuthUser authUser) {
        Gathering gathering = findGathering(request.gatheringId());
        List<String> optionNames = request.optionNames();
        validateCanCreateVote(optionNames, request, authUser);

        Vote vote = Vote.builder()
                .gathering(gathering)
                .title(request.title())
                .build();
        voteRepository.save(vote);
        optionNames.stream()
                .forEach((name) -> optionRepository.save(Option.builder()
                            .content(name)
                            .vote(vote)
                            .build()));
        return vote.getId();
    }

    private void validateCanCreateVote(
            List<String> optionNames,
            VoteCreateRequest request,
            AuthUser authUser
    ) {
        validateUserInGathering(request.gatheringId(), authUser);
        validateNumberOfOptions(optionNames);
    }

    private void validateNumberOfOptions(List<String> optionNames) {
        if (optionNames.size() < MIN_NUMBER_OF_OPTIONS) {
            throw new BusinessException(TOO_FEW_OPTIONS);
        }
    }

    public void vote(Long voteId, VoteRequest request, AuthUser authUser) {
        Vote vote = findVote(voteId);
        Option option = findOption(request);
        validateCanVote(vote, authUser, request);
        option.vote();
    }

    private void validateCanVote(
            Vote vote,
            AuthUser authUser,
            VoteRequest request
    ) {
        validateVotePeriod(vote);
        validateUserInGathering(vote.getGathering().getId(), authUser);
        validateOptionInVote(request.optionId(), vote.getId());
    }

    private void validateVotePeriod(Vote vote) {
        if (vote.isEnd()) {
            throw new BusinessException(ALREADY_VOTE_END);
        }
    }

    private Option findOption(VoteRequest request) {
        return optionRepository.findById(request.optionId())
                .orElseThrow(() -> new BusinessException(OPTION_NOT_FOUND));
    }

    private void validateOptionInVote(Long optionId, Long voteId) {
        optionRepository.existsByIdAndVoteId(optionId, voteId);
    }

    private Vote findVote(Long voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new BusinessException(VOTE_NOT_FOUND));
    }

    private void validateUserInGathering(Long gatheringId, AuthUser authUser) {
        if (!gatheringRepository.existsByGatheringIdAndUserId(gatheringId, authUser.getId())) {
            throw new BusinessException(USER_NOT_IN_GATHERING);
        }
    }

    private Gathering findGathering(Long gatheringId) {
        return gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new BusinessException(GATHERING_NOT_FOUND));
    }
}
