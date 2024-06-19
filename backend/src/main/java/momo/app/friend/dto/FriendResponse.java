package momo.app.friend.dto;

import lombok.Getter;
import momo.app.friend.domain.Friend;
import momo.app.user.domain.User;

public record FriendResponse(
        Long id,
        Long userId,
        String nickName
) {
    public static FriendResponse of(Friend friend, boolean isToUser) {
        if (isToUser) {
            return new FriendResponse(
                    friend.getId(),
                    friend.getToUser().getId(),
                    friend.getToUser().getNickname()
            );
        } else {
            return new FriendResponse(
                    friend.getId(),
                    friend.getFromUser().getId(),
                    friend.getFromUser().getNickname()
            );
        }
    }
}
