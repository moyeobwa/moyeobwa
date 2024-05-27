package momo.app.friend.service;

import momo.app.auth.dto.AuthUser;
import momo.app.common.error.exception.BusinessException;
import momo.app.friend.domain.Friend;
import momo.app.friend.domain.FriendRepository;
import momo.app.friend.domain.FriendState;
import momo.app.friend.exception.FriendErrorCode;
import momo.app.user.domain.Role;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import static momo.app.auth.dto.AuthUser.createAuthUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
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
        user1 = new User(1L, "socialId1", "User1", "user1", "user1@example.com", "image1", "refreshToken1", Role.GUEST, "active", "desc1", "interest01");
        user2 = new User(2L, "socialId2", "User2", "user2", "user2@example.com", "image2", "refreshToken2", Role.GUEST, "active", "desc2", "interest02");
    }

    @Test
    void testRequest() {
        AuthUser authUser = createAuthUser(user1);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        friendService.request(2L, authUser);

        verify(friendRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(user1.getId());
        verify(userRepository, times(1)).findById(user2.getId());
    }

    @Test
    void testRequestExceptionOnSecondCall() {
        AuthUser authUser = createAuthUser(user1);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(friendRepository.findByTwoUser(any(), any())).thenThrow(new BusinessException(FriendErrorCode.FRIEND_REQUEST_ALREADY_EXISTS));

        assertThrows(BusinessException.class, () -> friendService.request(2L, authUser));

        verify(friendRepository, times(1)).findByTwoUser(any(), any());
    }

    @Test
    void testAccept() {
        AuthUser authUser = createAuthUser(user1);
        Friend friend = Friend.builder()
                .fromUser(user2)
                .toUser(user1)
                .friendState(FriendState.WAITING)
                .build();
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(friendRepository.findById(1L)).thenReturn(Optional.of(friend));

        friendService.accept(1L, authUser);

        verify(friendRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(user1.getId());
    }
}