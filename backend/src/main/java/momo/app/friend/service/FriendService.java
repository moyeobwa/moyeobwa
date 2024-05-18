package momo.app.friend.service;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.friend.domain.Friend;
import momo.app.friend.domain.FriendRepository;
import momo.app.friend.domain.FriendState;
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

        validateFriendRequest(fromUser, toUser);

        Friend fromFriend = Friend.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .friendState(FriendState.WAITING)
                .build();
        friendRepository.save(fromFriend);
        fromUser.addFriend(fromFriend);
    }

    public void accept(Long id, AuthUser authUser) {
        Friend fromFriend = findFriend(id);
        User toUser = findUser(authUser.getId());
        User fromUser = fromFriend.getFromUser();

        fromFriend.accept();
        Friend toFriend = Friend.builder()
                .fromUser(toUser)
                .toUser(fromUser)
                .friendState(FriendState.ACCEPT)
                .build();
        friendRepository.save(toFriend);
        fromUser.addFriend(toFriend);
    }

    public void reject(Long id) {
        Friend friendRequest = friendRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Friend request not found"));

        friendRepository.delete(friendRequest);
    }

    public List<FriendResponse> getFriends(AuthUser authUser) {
        User user = findUser(authUser.getId());

        List<Friend> friends = friendRepository.findAllByFromUser(user);

        return friends.stream()
                .sorted(Comparator.comparingLong(Friend::getId))
                .map(FriendResponse::from)
                .collect(Collectors.toList());
    }

    public List<FriendResponse> getRequest(AuthUser authUser) {
        User user = findUser(authUser.getId());

        List<Friend> requestFriends = friendRepository.findByToUserAndState(user, FriendState.WAITING);

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

    private Friend findFriend(Long id) {
        User user = findUser(id);

        return friendRepository.findByFromUser(user)
                .orElseThrow(() -> new NoSuchElementException("friend not found"));
    }

    private void validateFriendRequest(User fromUser, User toUser) {
        Friend fromUsersFriend = friendRepository.findByFromUserAndToUser(fromUser, toUser).orElse(null);
        if(fromUsersFriend != null) {
            if(fromUsersFriend.getFriendState() != FriendState.WAITING) {
                throw new RuntimeException("이미 요청한 친구신청입니다.");
            } else {
                throw new RuntimeException("이미 친구입니다.");
            }
        }

        Friend toUsersFriend = friendRepository.findByFromUserAndToUser(fromUser, toUser).orElse(null);
        if(toUsersFriend != null) {
            if(toUsersFriend.getFriendState() != FriendState.WAITING) {
                throw new RuntimeException("이미 요청받은 친구신청입니다.");
            } else {
                throw new RuntimeException("이미 친구입니다.");
            }
        }
    }
}
