package momo.app.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.auth.dto.AuthUser;
import momo.app.auth.jwt.service.JwtCreateAndUpdateService;
import momo.app.auth.jwt.service.JwtExtractService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/tokens")
public class TokenController {

    private final JwtCreateAndUpdateService jwtCreateAndUpdateService;
    private final JwtExtractService jwtExtractService;

    @GetMapping
    public ResponseEntity<String> createToken(HttpServletRequest request) {
        String refreshToken = jwtExtractService.extractRefreshToken(request)
                .orElse(null);
        String email = jwtExtractService.extractEmail(refreshToken)
                        .orElse(null);
        String token = jwtCreateAndUpdateService.createAccessToken(email);
        return ResponseEntity.ok(token);
    }
}
