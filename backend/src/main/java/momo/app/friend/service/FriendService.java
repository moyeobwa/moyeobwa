package momo.app.friend.service;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.common.error.exception.BusinessException;
import momo.app.friend.domain.Friend;
import momo.app.friend.domain.FriendRepository;
import momo.app.friend.domain.FriendState;
import momo.app.friend.dto.FriendResponse;
import momo.app.friend.exception.FriendErrorCode;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import momo.app.user.exception.UserErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
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
        Friend requestFriend = findFriend(id);
        User fromUser = findUser(authUser.getId());
        User toUser = requestFriend.getFromUser();

        requestFriend.accept();
        Friend acceptFriend = Friend.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .friendState(FriendState.ACCEPT)
                .build();
        friendRepository.save(acceptFriend);
        fromUser.addFriend(acceptFriend);
    }

    public void reject(Long id, AuthUser authUser) {
        Friend friendRequest = findFriend(id);
        User user = findUser(authUser.getId());

        validateFriendDelete(user, friendRequest);

        friendRepository.delete(friendRequest);
    }

    public void delete(Long id, AuthUser authUser) {
        Friend fromFriend = findFriend(id);
        User user = findUser(authUser.getId());

        validateFriendDelete(user, fromFriend);

        Friend toFriend = friendRepository.findByFromUser(fromFriend.getToUser())
                .orElseThrow(() -> new BusinessException(FriendErrorCode.FRIEND_NOT_FOUND));

        friendRepository.delete(fromFriend);
        friendRepository.delete(toFriend);
    }

    public List<FriendResponse> getFriends(AuthUser authUser) {
        User user = findUser(authUser.getId());

        List<Friend> friends = friendRepository.findAllByFromUserAndState(user, FriendState.ACCEPT);

        return friends.stream()
                .sorted(Comparator.comparingLong(Friend::getId))
                .map(FriendResponse::from)
                .collect(Collectors.toList());
    }

    public List<FriendResponse> getRequest(AuthUser authUser) {
        User user = findUser(authUser.getId());

        List<Friend> requestFriends = friendRepository.findAllByToUserAndState(user, FriendState.WAITING);

        return requestFriends.stream()
                .sorted(Comparator.comparingLong(Friend::getId))
                .map(FriendResponse::from)
                .collect(Collectors.toList());
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(UserErrorCode.User_NOT_FOUND));
    }

    private Friend findFriend(Long id) {
        return friendRepository.findById(id)
                .orElseThrow(() -> new BusinessException(FriendErrorCode.FRIEND_NOT_FOUND));
    }

    private void validateFriendRequest(User fromUser, User toUser) {
        List<Friend> friends = friendRepository.findByTwoUser(fromUser, toUser);

        if (friends.size() == 1) {
            throw new BusinessException(FriendErrorCode.FRIEND_REQUEST_ALREADY_EXISTS);
        } else if (friends.size() == 2) {
            throw new BusinessException(FriendErrorCode.FRIEND_ALREADY_EXISTS);
        }
    }

    private void validateFriendDelete(User user, Friend friend) {
        if (!user.equals(friend.getFromUser()) || !user.equals(friend.getToUser())) {
            throw new BusinessException(FriendErrorCode.FRIEND_DELETE_PERMISSION_DENIED);
        }
    }
}
