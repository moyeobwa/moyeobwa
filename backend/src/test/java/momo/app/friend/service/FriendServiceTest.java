package momo.app.friend.service;

import momo.app.auth.dto.AuthUser;
import momo.app.friend.domain.Friend;
import momo.app.friend.domain.FriendRepository;
import momo.app.friend.domain.State;
import momo.app.user.domain.Role;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import static momo.app.auth.dto.AuthUser.createAuthUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class FriendServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRepository friendRepository;

    @InjectMocks
    private FriendService friendService;

    private User user1;
    private User user2;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        user1 = new User(1L, "socialId1", "User1", "user1", "user1@example.com", "image1", "refreshToken1", Role.GUEST, "active", "desc1", "interest1", new ArrayList<>());
        user2 = new User(2L, "socialId2", "User2", "user2", "user2@example.com", "image2", "refreshToken2", Role.GUEST, "active", "desc2", "interest2", new ArrayList<>());
    }

    @Test
    void testRequest() {
        AuthUser authUser = createAuthUser(user1);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findByNickname("user2")).thenReturn(Optional.of(user2));
        when(friendRepository.findByUserAndFriendAndState(any(), any(), any())).thenReturn(Optional.empty());

        friendService.request("user2", authUser);

        verify(friendRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(user1.getId());
        verify(userRepository, times(1)).findByNickname("user2");
    }

    @Test
    void testRequestRuntimeExceptionOnSecondCall() {
        AuthUser authUser = createAuthUser(user1);


        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findByNickname("user2")).thenReturn(Optional.of(user2));
        when(friendRepository.findByUserAndFriendAndState(any(), any(), any())).thenReturn(Optional.empty());

        // 첫 번째 호출
        friendService.request("user2", authUser);

        when(friendRepository.findByUserAndFriendAndState(any(), any(), any()))
                .thenReturn(Optional.of(Friend.builder()
                        .user(user1)
                        .friend(user2)
                        .state(State.WAITING)
                        .build()));

        // 두 번째 호출
        assertThrows(RuntimeException.class, () -> friendService.request("user2", authUser));

        verify(friendRepository, times(5)).findByUserAndFriendAndState(any(), any(), any());
    }

    @Test
    void testAccept() {
        AuthUser authUser = createAuthUser(user1);
        Friend friend = Friend.builder()
                .user(user2)
                .friend(user1)
                .state(State.WAITING)
                .build();
        when(userRepository.findByNickname("user2")).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser(user2)).thenReturn(Optional.of(friend));
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        friendService.accept("user2", authUser);

        verify(friendRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(user1.getId());
    }
}