package momo.app.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.auth.jwt.service.JwtExtractService;
import momo.app.user.domain.Role;
import momo.app.user.domain.User;
import momo.app.user.service.UserService;
import momo.app.user.dto.request.UserSignupJsonRequest;
import momo.app.user.dto.request.UserSignupRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class LoginController {
    private final UserService userService;
    private final JwtExtractService jwtExtractService;

    @GetMapping("/role")
    public ResponseEntity<Role> getRole(
            @AuthenticationPrincipal AuthUser authUser,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(userService.getRole(authUser));
    }

    @PostMapping(value = "/sign-up",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> signUp(
            @RequestPart MultipartFile image,
            @RequestPart UserSignupJsonRequest request,
            @AuthenticationPrincipal AuthUser authUser,
            HttpServletResponse response
    ) {

        UserSignupRequest userSignupRequest = UserSignupRequest.of(request, image);
        userService.signUp(userSignupRequest, response, authUser);

        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal AuthUser authUser,
            HttpServletRequest request
    ) {
        String accessToken = jwtExtractService.extractAccessToken(request).orElseThrow(
                () -> new NoSuchElementException("Access 토큰이 존재하지 않습니다."));
        userService.logout(accessToken, authUser);

        return ResponseEntity.ok()
                .build();
    }
}
