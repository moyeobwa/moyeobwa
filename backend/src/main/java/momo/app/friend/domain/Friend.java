package momo.app.friend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import momo.app.common.error.exception.BusinessException;
import momo.app.friend.exception.FriendErrorCode;
import momo.app.user.domain.User;

@Entity
@Getter
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
    private FriendState friendState;

    @Builder
    public Friend(User fromUser, User toUser, FriendState friendState) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.friendState = friendState;
    }

    public void accept() {
        this.friendState = FriendState.ACCEPT;
    }

    public void validateFriendDelete(User user, Friend friend) {
        if (!user.equals(friend.getFromUser()) || !user.equals(friend.getToUser())) {
            throw new BusinessException(FriendErrorCode.FRIEND_DELETE_PERMISSION_DENIED);
        }
    }
}