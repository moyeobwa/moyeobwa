package momo.app.auth.controller;

import lombok.RequiredArgsConstructor;
import momo.app.auth.jwt.service.JwtCreateAndUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/testTokens")
public class TestTokenController {

    private static final String ADMIN_USER_EMAIL = "admin@moyeobwa.com";

    private final JwtCreateAndUpdateService jwtCreateAndUpdateService;

    @GetMapping
    public ResponseEntity<String> createTestToken() {
        String token = jwtCreateAndUpdateService.createAccessToken(ADMIN_USER_EMAIL);
        return ResponseEntity.ok(token);
    }
}
