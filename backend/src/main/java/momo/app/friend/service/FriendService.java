package momo.app.friend.service;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.common.error.exception.BusinessException;
import momo.app.friend.domain.Friend;
import momo.app.friend.domain.FriendRepository;
import momo.app.friend.domain.FriendState;
import momo.app.friend.dto.FriendResponse;
import momo.app.friend.exception.FriendErrorCode;
import momo.app.gathering.domain.*;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static momo.app.friend.exception.FriendErrorCode.*;
import static momo.app.gathering.exception.GatheringErrorCode.*;
import static momo.app.user.exception.UserErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final GatheringRepository gatheringRepository;
    private final GatheringInviteRepository gatheringInviteRepository;
    private final GatheringMemberRepository gatheringMemberRepository;

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
    }

    public FriendResponse accept(Long id, AuthUser authUser) {
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
        return FriendResponse.of(acceptFriend, true);
    }

    public void reject(Long id, AuthUser authUser) {
        Friend friendRequest = findFriend(id);
        User user = findUser(authUser.getId());

        friendRequest.validateUserInFriend(user);

        friendRepository.delete(friendRequest);
    }

    public void cancel(Long id, AuthUser authUser) {
        Friend friendRequest = findFriend(id);
        User user = findUser(authUser.getId());

        friendRequest.validateUserInFriend(user);

        friendRepository.deleteById(friendRequest.getId());
    }

    public void delete(Long id, AuthUser authUser) {
        Friend fromFriend = findFriend(id);
        User user = findUser(authUser.getId());

        fromFriend.validateUserInFriend(user);

        Friend toFriend = friendRepository.findByFromUser(fromFriend.getToUser())
                .orElseThrow(() -> new BusinessException(FRIEND_NOT_FOUND));

        friendRepository.deleteAllByIds(List.of(fromFriend.getId(), toFriend.getId()));
    }

    public List<FriendResponse> getFriends(AuthUser authUser) {
        User user = findUser(authUser.getId());

        List<Friend> friends = friendRepository.findAllByFromUserAndState(user, FriendState.ACCEPT);

        return friends.stream()
                .sorted(Comparator.comparingLong(Friend::getId))
                .map(friend -> FriendResponse.of(friend, true)) // toUser를 사용하여 FriendResponse 생성
                .collect(Collectors.toList());
    }

    public List<FriendResponse> getUserRequests(AuthUser authUser) {
        User user = findUser(authUser.getId());

        List<Friend> requestFriends = friendRepository.findAllByFromUserAndState(user, FriendState.WAITING);

        return requestFriends.stream()
                .sorted(Comparator.comparingLong(Friend::getId))
                .map(friend -> FriendResponse.of(friend, true)) // toUser를 사용하여 FriendResponse 생성
                .collect(Collectors.toList());
    }

    public List<FriendResponse> getFriendRequests(AuthUser authUser) {
        User user = findUser(authUser.getId());

        List<Friend> requestFriends = friendRepository.findAllByToUserAndState(user, FriendState.WAITING);

        return requestFriends.stream()
                .sorted(Comparator.comparingLong(Friend::getId))
                .map(friend -> FriendResponse.of(friend, false)) // toUser를 사용하여 FriendResponse 생성
                .collect(Collectors.toList());
    }

    public Long inviteFriend(
            Long gatheringId,
            Long friendId,
            AuthUser authUser
    ) {
        User fromUser = findUser(authUser.getId());
        User toUser = findUser(friendId);
        Gathering gathering = findGathering(gatheringId);

        validateFriend(fromUser, toUser);
        validateGatheringInvite(fromUser, toUser);

        GatheringInvite gatheringInvite = GatheringInvite.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .gathering(gathering)
                .build();

        gatheringInviteRepository.save(gatheringInvite);

        return gatheringInvite.getId();
    }

    public void acceptInvite(Long gatheringInviteId, AuthUser authUser) {
        User user = findUser(authUser.getId());
        GatheringInvite gatheringInvite = findGatheringInvite(gatheringInviteId);

        gatheringInvite.validateToUser(authUser);

        GatheringMember gatheringMember = GatheringMember.builder()
                .user(user)
                .gathering(gatheringInvite.getGathering())
                .build();

        gatheringMemberRepository.save(gatheringMember);
        gatheringInviteRepository.deleteById(gatheringInviteId);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

    private Friend findFriend(Long id) {
        return friendRepository.findById(id)
                .orElseThrow(() -> new BusinessException(FRIEND_NOT_FOUND));
    }

    private Gathering findGathering(Long id) {
        return gatheringRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GATHERING_NOT_FOUND));
    }

    private GatheringInvite findGatheringInvite(Long id) {
        return gatheringInviteRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GATHERING_INVITE_NOT_FOUND));
    }

    private void validateGatheringInvite(User fromUser, User toUser) {
        List<GatheringInvite> gatheringInvites = gatheringInviteRepository.findByTwoUser(fromUser, toUser);

        if (gatheringInvites.size() >= 0) {
            throw new BusinessException(GATHERING_INVITE_ALREADY_EXISTS);
        }
    }

    private void validateFriendRequest(User fromUser, User toUser) {
        if (fromUser.getId() == toUser.getId()) {
            throw new BusinessException(FRIEND_REQUEST_SELF);
        }

        List<Friend> friends = friendRepository.findByTwoUser(fromUser, toUser);

        if (friends.size() == 1) {
            throw new BusinessException(FRIEND_REQUEST_ALREADY_EXISTS);
        } else if (friends.size() == 2) {
            throw new BusinessException(FRIEND_ALREADY_EXISTS);
        }
    }

    private void validateFriend(User fromUser, User toUser) {

        List<Friend> friends = friendRepository.findByTwoUser(fromUser, toUser);

        if (friends.size() <= 0) {
            throw new BusinessException(FRIEND_NOT_FOUND);
        }
    }
}
