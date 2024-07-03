package momo.app.vote.service;

import static momo.app.gathering.exception.GatheringErrorCode.GATHERING_NOT_FOUND;
import static momo.app.gathering.exception.GatheringErrorCode.USER_NOT_IN_GATHERING;
import static momo.app.user.exception.UserErrorCode.USER_NOT_FOUND;
import static momo.app.vote.exception.OptionErrorCode.OPTION_NOT_FOUND;
import static momo.app.vote.exception.OptionErrorCode.TOO_FEW_OPTIONS;
import static momo.app.vote.exception.VoteErrorCode.ALREADY_VOTE;
import static momo.app.vote.exception.VoteErrorCode.ALREADY_VOTE_END;
import static momo.app.vote.exception.VoteErrorCode.VOTE_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.common.error.exception.BusinessException;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import momo.app.vote.domain.Option;
import momo.app.vote.domain.OptionRepository;
import momo.app.vote.domain.UserOption;
import momo.app.vote.domain.UserOptionRepository;
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
    private final UserRepository userRepository;
    private final UserOptionRepository userOptionRepository;

    public Long create(VoteCreateRequest request, AuthUser authUser) {
        Gathering gathering = findGathering(request.gatheringId());
        List<String> optionNames = request.optionNames();
        validateCanCreateVote(optionNames, request, authUser);

        Vote vote = voteRepository.save(Vote.builder()
                .gathering(gathering)
                .title(request.title())
                .creatorId(authUser.getId())
                .build());
        optionNames.stream()
                .forEach((name) -> optionRepository.save(Option.builder()
                            .content(name)
                            .vote(vote)
                            .build()));
        gathering.activate();

        return vote.getId();
    }

    public void vote(Long voteId, VoteRequest request, AuthUser authUser) {
        Vote vote = findVote(voteId);
        User user = findUser(authUser.getId());
        Option option = findOption(request);
        validateCanVote(vote, user, request);
        userOptionRepository.save(UserOption.builder()
                .option(option)
                .user(user)
                .build());
        option.vote();
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

    private void validateCanCreateVote(
            List<String> optionNames,
            VoteCreateRequest request,
            AuthUser authUser
    ) {
        validateUserInGathering(request.gatheringId(), authUser.getId());
        validateNumberOfOptions(optionNames);
    }

    private void validateNumberOfOptions(List<String> optionNames) {
        if (optionNames.size() < MIN_NUMBER_OF_OPTIONS) {
            throw new BusinessException(TOO_FEW_OPTIONS);
        }
    }

    private void validateCanVote(
            Vote vote,
            User user,
            VoteRequest request
    ) {
        validateVotePeriod(vote);
        validateUserInGathering(vote.getGathering().getId(), user.getId());
        validateOptionInVote(request.optionId(), vote.getId());
        validateDuplicateVote(vote, user);
    }

    private void validateDuplicateVote(Vote vote, User user) {
        if (userOptionRepository.checkByVoteAndUser(vote, user)) {
            throw new BusinessException(ALREADY_VOTE);
        }
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
        optionRepository.checkExistsByIdAndVoteId(optionId, voteId);
    }

    private Vote findVote(Long voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new BusinessException(VOTE_NOT_FOUND));
    }

    private void validateUserInGathering(Long gatheringId, Long userId) {
        if (!gatheringRepository.checkExistsByGatheringIdAndUserId(gatheringId, userId)) {
            throw new BusinessException(USER_NOT_IN_GATHERING);
        }
    }

    private Gathering findGathering(Long gatheringId) {
        return gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new BusinessException(GATHERING_NOT_FOUND));
    }
}
