package momo.app.friend.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendState {
    ACCEPT("STATE_ACCEPT"), WAITING("STATE_WAITING");

    private final String key;
}
