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

    public void request(String nickName, AuthUser authUser) {
        User fromUser = findUser(authUser.getId());
        User toUser = findUserByNickName(nickName);

        validateFriendRequest(fromUser, toUser);

        Friend fromFriend = Friend.builder()
                .user(fromUser)
                .friend(toUser)
                .state(State.WAITING)
                .build();
        friendRepository.save(fromFriend);
        fromUser.addFriend(fromFriend);
    }

    public void accept(String nickName, AuthUser authUser) {
        Friend fromFriend = findFriendByNickName(nickName);
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

        List<Friend> requestFriends = friendRepository.findByFriendAndState(user, State.WAITING);

        return requestFriends.stream()
                .sorted(Comparator.comparingLong(Friend::getId))
                .map(FriendResponse::from)
                .collect(Collectors.toList());
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("user not found"));
    }

    private User findUserByNickName(String nickName) {
        return userRepository.findByNickname(nickName)
                .orElseThrow(() -> new NoSuchElementException("user not found"));
    }

    private Friend findFriendByNickName(String nickName) {
        User user = findUserByNickName(nickName);

        return friendRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("friend not found"));
    }

    private void validateFriendRequest(User fromUser, User toUser) {
        if(friendRepository.findByUserAndFriendAndState(fromUser, toUser, State.WAITING)
                .isPresent()) {
            throw new RuntimeException("이미 요청한 친구신청입니다.");
        }
        if(friendRepository.findByUserAndFriendAndState(toUser, fromUser, State.WAITING)
                .isPresent()) {
            throw new RuntimeException("이미 요청받은 친구신청입니다.");
        }
        if(friendRepository.findByUserAndFriendAndState(fromUser, toUser, State.ACCEPT)
                .isPresent()) {
            throw new RuntimeException("이미 친구입니다.");
        }
        if(friendRepository.findByUserAndFriendAndState(toUser, fromUser, State.ACCEPT)
                .isPresent()) {
            throw new RuntimeException("이미 친구입니다.");
        }
    }
}
