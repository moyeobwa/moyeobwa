package momo.app.friend.service;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.friend.domain.Friend;
import momo.app.friend.domain.FriendRepository;
import momo.app.friend.domain.State;
import momo.app.friend.dto.FriendResponse;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public void request(Long id, AuthUser authUser) {
        User fromUser = findUser(authUser.getId());
        User toUser = findUser(id);

        Friend fromFriend = Friend.builder()
                .user(fromUser)
                .friend(toUser)
                .state(State.WAITING)
                .build();
        friendRepository.save(fromFriend);
        fromUser.addFriend(fromFriend);
    }

    public void accept(Long id, AuthUser authUser) {
        Friend fromFriend = findFriend(id);
        User toUser = findUser(authUser.getId());
        User fromUser = fromFriend.getUser();

        fromFriend.acceptFriendRequest();
        Friend toFriend = Friend.builder()
                .user(toUser)
                .friend(fromUser)
                .state(State.ACCEPT)
                .build();
        friendRepository.save(toFriend);
        fromUser.addFriend(toFriend);
    }

    public List<FriendResponse> getFriends(AuthUser authUser) {
        User user = findUser(authUser.getId());

        List<Friend> friends = friendRepository.findAllByUser(user);

        return friends.stream()
                .sorted(Comparator.comparingLong(Friend::getId))
                .map(FriendResponse::from)
                .collect(Collectors.toList());
    }

    public List<FriendResponse> getRequest(AuthUser authUser) {
        User user = findUser(authUser.getId());

        List<Friend> requestFriends = friendRepository.findByFriendAndState(user);

        return requestFriends.stream()
                .sorted(Comparator.comparingLong(Friend::getId))
                .map(FriendResponse::from)
                .collect(Collectors.toList());
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("user not found"));
    }

    private Friend findFriend(Long id) {
        return friendRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("friend not found"));
    }
}
