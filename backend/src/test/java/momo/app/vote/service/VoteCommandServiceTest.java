package momo.app.vote.service;

import static momo.app.gathering.exception.GatheringErrorCode.USER_NOT_IN_GATHERING;
import static momo.app.vote.exception.OptionErrorCode.TOO_FEW_OPTIONS;
import static momo.app.vote.exception.VoteErrorCode.ALREADY_VOTE_END;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import momo.app.auth.dto.AuthUser;
import momo.app.common.error.exception.BusinessException;
import momo.app.gathering.domain.Category;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringInfo;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.user.domain.Role;
import momo.app.user.domain.User;
import momo.app.vote.domain.Option;
import momo.app.vote.domain.OptionRepository;
import momo.app.vote.domain.Vote;
import momo.app.vote.domain.VoteRepository;
import momo.app.vote.dto.VoteCreateRequest;
import momo.app.vote.dto.VoteRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VoteCommandServiceTest {

    @Mock
    VoteRepository voteRepository;

    @Mock
    GatheringRepository gatheringRepository;

    @Mock
    OptionRepository optionRepository;

    @InjectMocks
    VoteCommandService voteCommandService;

    @Test
    void 모임에_참가중인_유저는_투표를_생성할_수_있다() {
        User user = User.builder()
                .email("이메일")
                .role(Role.USER)
                .id(1L)
                .build();

        Gathering gathering = Gathering.builder()
                .managerId(1L)
                .gatheringInfo(GatheringInfo.of(Category.EXERCISE, "모임이름1", "모임설명1", "https://qwe.png"))
                .chatRoomId(1L)
                .build();

        Vote vote = Vote.builder()
                .gathering(gathering)
                .creatorId(user.getId())
                .title("투표1")
                .build();

        Option option = Option.builder()
                .content("옵션1")
                .vote(vote)
                .build();

        VoteCreateRequest request = new VoteCreateRequest("투표1", 1L, List.of("옵션1", "옵션2", "옵션3"));
        AuthUser authUser = AuthUser.createAuthUser(user);

        given(gatheringRepository.findById(1L)).willReturn(Optional.of(gathering));
        given(voteRepository.save(any())).willReturn(vote);
        given(optionRepository.save(any())).willReturn(option);
        given(gatheringRepository.checkExistsByGatheringIdAndUserId(any(), any())).willReturn(true);

        Long voteId = voteCommandService.create(request, authUser);
        Assertions.assertThat(voteId).isEqualTo(vote.getId());
    }

    @Test
    void 투표를_생성할_때_옵션이_2개_미만이면_예외가_발생한다() {
        User user = User.builder()
                .email("이메일")
                .role(Role.USER)
                .id(1L)
                .build();

        Gathering gathering = Gathering.builder()
                .managerId(1L)
                .gatheringInfo(GatheringInfo.of(Category.EXERCISE, "모임이름1", "모임설명1", "https://qwe.png"))
                .chatRoomId(1L)
                .build();

        VoteCreateRequest request = new VoteCreateRequest("투표1", 1L, List.of("옵션1"));
        AuthUser authUser = AuthUser.createAuthUser(user);

        given(gatheringRepository.findById(1L)).willReturn(Optional.of(gathering));
        given(gatheringRepository.checkExistsByGatheringIdAndUserId(any(), any())).willReturn(true);

        Assertions.assertThatThrownBy(() -> voteCommandService.create(request, authUser))
                .isInstanceOf(BusinessException.class)
                .hasMessage(TOO_FEW_OPTIONS.getMessage());
    }

    @Test
    void 모임방에_없는_유저가_투표를_생성하면_예외가_발생한다() {
        User user2 = User.builder()
                .email("이메일2")
                .role(Role.USER)
                .id(2L)
                .build();

        Gathering gathering = Gathering.builder()
                .managerId(1L)
                .gatheringInfo(GatheringInfo.of(Category.EXERCISE, "모임이름1", "모임설명1", "https://qwe.png"))
                .chatRoomId(1L)
                .build();

        VoteCreateRequest request = new VoteCreateRequest("투표1", 1L, List.of("옵션1"));
        AuthUser authUser = AuthUser.createAuthUser(user2);

        given(gatheringRepository.findById(1L)).willReturn(Optional.of(gathering));
        given(gatheringRepository.checkExistsByGatheringIdAndUserId(any(), any())).willReturn(false);

        Assertions.assertThatThrownBy(() -> voteCommandService.create(request, authUser))
                .isInstanceOf(BusinessException.class)
                .hasMessage(USER_NOT_IN_GATHERING.getMessage());
    }

    @Test
    void 모임방에_존재하는_유저는_투표에_참여할_수_있다() {
        User user = User.builder()
                .email("이메일")
                .role(Role.USER)
                .id(1L)
                .build();

        Gathering gathering = Gathering.builder()
                .managerId(1L)
                .gatheringInfo(GatheringInfo.of(Category.EXERCISE, "모임이름1", "모임설명1", "https://qwe.png"))
                .chatRoomId(1L)
                .build();

        Vote vote = Vote.builder()
                .gathering(gathering)
                .creatorId(user.getId())
                .title("투표1")
                .build();

        Option option = Option.builder()
                .content("옵션1")
                .vote(vote)
                .build();

        VoteRequest request = new VoteRequest(1L);
        AuthUser authUser = AuthUser.createAuthUser(user);

        given(voteRepository.findById(any())).willReturn(Optional.of(vote));
        given(optionRepository.findById(any())).willReturn(Optional.of(option));
        given(gatheringRepository.checkExistsByGatheringIdAndUserId(any(), any())).willReturn(true);
        given(optionRepository.checkExistsByIdAndVoteId(any(), any())).willReturn(true);

        voteCommandService.vote(1L, request, authUser);
        Assertions.assertThat(option.getCount()).isEqualTo(1);
    }

    @Test
    void 투표_기간이_지났으면_투표를_할_수_없다() {
        User user = User.builder()
                .email("이메일")
                .role(Role.USER)
                .id(1L)
                .build();

        Gathering gathering = Gathering.builder()
                .managerId(1L)
                .gatheringInfo(GatheringInfo.of(Category.EXERCISE, "모임이름1", "모임설명1", "https://qwe.png"))
                .chatRoomId(1L)
                .build();

        Vote vote = Vote.builder()
                .gathering(gathering)
                .creatorId(user.getId())
                .title("투표1")
                .build();
        vote.terminate();

        Option option = Option.builder()
                .content("옵션1")
                .vote(vote)
                .build();

        VoteRequest request = new VoteRequest(1L);
        AuthUser authUser = AuthUser.createAuthUser(user);

        given(voteRepository.findById(any())).willReturn(Optional.of(vote));
        given(optionRepository.findById(any())).willReturn(Optional.of(option));

        Assertions.assertThatThrownBy(() -> voteCommandService.vote(1L, request, authUser))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ALREADY_VOTE_END.getMessage());
    }
}