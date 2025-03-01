package momo.app.friend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.common.error.exception.BusinessException;
import momo.app.friend.exception.FriendErrorCode;
import momo.app.user.domain.User;

@Entity
@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Enumerated(EnumType.STRING)
    private FriendState state;

    @Builder
    public Friend(User fromUser, User toUser, FriendState friendState) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.state = friendState;
    }

    public void accept() {
        this.state = FriendState.ACCEPT;
    }

    public void validateUserInFriend(User user) {
        if (!user.getId().equals(this.fromUser.getId()) && !user.getId().equals(this.toUser.getId())) {
            throw new BusinessException(FriendErrorCode.USER_NOT_PART_OF_FRIEND);
        }
    }
}