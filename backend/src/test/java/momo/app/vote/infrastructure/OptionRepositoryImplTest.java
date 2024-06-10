package momo.app.vote.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import momo.app.DatabaseCleanup;
import momo.app.gathering.domain.Category;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringInfo;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.user.domain.Role;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import momo.app.vote.domain.Option;
import momo.app.vote.domain.OptionRepository;
import momo.app.vote.domain.Vote;
import momo.app.vote.domain.VoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OptionRepositoryImplTest {

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    GatheringRepository gatheringRepository;

    @Autowired
    DatabaseCleanup databaseCleanup;

    User user;

    Gathering gathering;

    Vote vote1;

    Vote vote2;

    Option optionInVote;

    Option optionNotInVote;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        user = userRepository.save(User.builder()
                .email("email")
                .nickname("nickname")
                .role(Role.USER)
                .socialId("1234")
                .build());

        gathering = gatheringRepository.save(Gathering.builder()
                .gatheringInfo(GatheringInfo.of(Category.EXERCISE, "모임1", "운동모임", "https://qwer.png"))
                .chatRoomId(1L)
                .managerId(user.getId())
                .build());

        vote1 = voteRepository.save(Vote.builder()
                .gathering(gathering)
                .creatorId(user.getId())
                .title("투표1")
                .build());

        vote1 = voteRepository.save(Vote.builder()
                .gathering(gathering)
                .creatorId(user.getId())
                .title("투표2")
                .build());

        optionInVote = optionRepository.save(Option.builder()
                .vote(vote1)
                .content("옵션1")
                .build());

        optionNotInVote = optionRepository.save(Option.builder()
                .vote(vote2)
                .content("옵션1")
                .build());
    }

    @Test
    void 옵션이_투표에_속해있으면_ture를_반환한다() {
        boolean isInVote = optionRepository.checkExistsByIdAndVoteId(optionInVote.getId(), vote1.getId());
        Assertions.assertThat(isInVote).isTrue();
    }

    @Test
    void 옵션이_투표에_속하지_않으면_false를_반환한다() {
        boolean isInVote = optionRepository.checkExistsByIdAndVoteId(optionNotInVote.getId(), vote1.getId());
        Assertions.assertThat(isInVote).isFalse();
    }
}