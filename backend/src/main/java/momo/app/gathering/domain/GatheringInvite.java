package momo.app.gathering.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.common.domain.BaseTime;
import momo.app.common.error.exception.BusinessException;
import momo.app.user.domain.User;

import static momo.app.gathering.exception.GatheringErrorCode.NOT_MANAGER;
import static momo.app.gathering.exception.GatheringErrorCode.NOT_TO_USER;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GatheringInvite extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @Builder
    public GatheringInvite(User fromUser, User toUser, Gathering gathering) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.gathering = gathering;
    }

    public void validateToUser(AuthUser authUser) {
        if (authUser.getId() != toUser.getId()) {
            throw new BusinessException(NOT_TO_USER);
        }
    }
}
