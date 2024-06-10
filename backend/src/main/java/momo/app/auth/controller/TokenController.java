package momo.app.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.auth.dto.AuthUser;
import momo.app.auth.exception.TokenErrorCode;
import momo.app.auth.jwt.service.JwtCreateAndUpdateService;
import momo.app.auth.jwt.service.JwtExtractService;
import momo.app.common.error.exception.BusinessException;
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
        String email = jwtExtractService.extractEmail(
                jwtExtractService.extractRefreshToken(request)
                        .orElseThrow(() -> new BusinessException(TokenErrorCode.TOKEN_NOT_FOUND))
        ).orElseThrow(() -> new BusinessException(TokenErrorCode.EMAIL_NOT_FOUND));

        jwtCreateAndUpdateService.updateRefreshToken(email, jwtCreateAndUpdateService.createRefreshToken(email));

        return ResponseEntity.ok(jwtCreateAndUpdateService.createAccessToken(email));
    }
}
