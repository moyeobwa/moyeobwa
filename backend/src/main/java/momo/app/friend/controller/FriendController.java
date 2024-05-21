package momo.app.friend.controller;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.friend.dto.FriendResponse;
import momo.app.friend.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/request/{id}")
    ResponseEntity<Void> request(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        friendService.request(id, authUser);

        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/accept/{id}")
    ResponseEntity<Void> accept(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        friendService.accept(id, authUser);

        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/reject/{id}")
    ResponseEntity<Void> reject(@PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {
        friendService.reject(id, authUser);

        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {
        friendService.delete(id, authUser);

        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/")
    ResponseEntity<List<FriendResponse>> getFriends(@AuthenticationPrincipal AuthUser authUser) {
        List<FriendResponse> friendResponses = friendService.getFriends(authUser);

        return ResponseEntity.ok(friendResponses);
    }

    @GetMapping("/requests")
    ResponseEntity<List<FriendResponse>> getRequests(@AuthenticationPrincipal AuthUser authUser) {
        List<FriendResponse> friendResponses = friendService.getRequest(authUser);

        return ResponseEntity.ok(friendResponses);
    }
}
