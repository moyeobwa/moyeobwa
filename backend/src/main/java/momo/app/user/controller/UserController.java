package momo.app.user.controller;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.user.dto.request.UserNicknameRequest;
import momo.app.user.dto.response.UserResponse;
import momo.app.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> search(
            UserNicknameRequest userNicknameRequest
    ) {
        String nickname = userNicknameRequest.nickname();
        return ResponseEntity.ok(userService.search(nickname));
    }
}
