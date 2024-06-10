package momo.app.auth.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtSendService {

    @Value("${jwt.access.header}")
    private String accessHeader; // access 헤더

    @Value("${jwt.refresh.header}")
    private String refreshHeader; // refresh 헤더

    //Http 헤더로 Access 토큰 보내기
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK); // 성공 상태
        response.setHeader(accessHeader, accessToken); // Http 헤더에 accessHeader를 키로 access 토큰 저장
        log.info("Access Token : {}", accessToken);
    }

    //Http 헤더로 Access 토큰과 Refresh 토큰 함께 보내기
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK); // 성공 상태
        response.setHeader(accessHeader, accessToken); // Http 헤더에 accessHeader를 키로 access 토큰 저장
        response.addHeader("Set-Cookie", createCookie(refreshHeader, refreshToken).toString());
        log.info("Access Token : {}, Refresh Token : {}", accessToken, refreshToken);
    }

    private ResponseCookie createCookie(String key, String value) {
        // 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .maxAge(14*24*60*60)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .build();

        return cookie;
    }
}
