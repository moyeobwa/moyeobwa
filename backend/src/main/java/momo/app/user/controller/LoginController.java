package momo.app.user.controller;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.user.service.UserService;
import momo.app.user.dto.request.UserSignupJsonRequest;
import momo.app.user.dto.request.UserSignupRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {
    private final UserService userService;

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
}
