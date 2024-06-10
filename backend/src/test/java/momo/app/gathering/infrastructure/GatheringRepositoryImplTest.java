package momo.app.gathering.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import momo.app.DatabaseCleanup;
import momo.app.gathering.domain.Category;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringInfo;
import momo.app.gathering.domain.GatheringMember;
import momo.app.gathering.domain.GatheringMemberRepository;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.user.domain.Role;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GatheringRepositoryImplTest {

    @Autowired
    GatheringRepository gatheringRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GatheringMemberRepository gatheringMemberRepository;

    @Autowired
    DatabaseCleanup databaseCleanup;

    User user1;

    User user2;

    Gathering gathering1;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        user1 = userRepository.save(User.builder()
                .email("email1")
                .nickname("nickname1")
                .role(Role.USER)
                .socialId("12341")
                .build());

        user2 = userRepository.save(User.builder()
                .email("email2")
                .nickname("nickname2")
                .role(Role.USER)
                .socialId("12342")
                .build());

        gathering1 = gatheringRepository.save(Gathering.builder()
                .gatheringInfo(GatheringInfo.of(Category.EXERCISE, "모임1", "운동모임", "https://qwer.png"))
                .chatRoomId(1L)
                .managerId(user1.getId())
                .build());

        gatheringMemberRepository.save(GatheringMember.builder()
                .gathering(gathering1)
                .user(user1)
                .build());
    }

    @Test
    void 모임에_속한_유저이면_true를_반환한다() {
        boolean isInGathering = gatheringRepository.checkExistsByGatheringIdAndUserId(gathering1.getId(), user1.getId());
        Assertions.assertThat(isInGathering).isTrue();
    }

    @Test
    void 모임에_속하지_않은_유저이면_false를_반환한다() {
        boolean isInGathering = gatheringRepository.checkExistsByGatheringIdAndUserId(gathering1.getId(), user2.getId());
        Assertions.assertThat(isInGathering).isFalse();
    }
}
