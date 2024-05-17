package momo.app.friend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import momo.app.user.domain.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private User friend;

    @Enumerated(EnumType.STRING)
    private State state;


    @Builder
    public Friend(User user, User friend, State state) {
        this.user = user;
        this.friend = friend;
        this.state = state;
    }

    public void acceptFriendRequest() {
        this.state = State.ACCEPT;
    }
}