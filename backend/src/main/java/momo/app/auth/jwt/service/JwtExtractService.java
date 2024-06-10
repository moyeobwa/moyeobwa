package momo.app.auth.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtExtractService {
    @Value("${jwt.secretKey}")
    private String secretKey; //jwt 비밀키

    @Value("${jwt.access.header}")
    private String accessHeader; // access 헤더

    @Value("${jwt.refresh.header}")
    private String refreshHeader; // refresh 헤더

    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    //클라이언트의 요청으로 access 토큰 header에서 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader)) // 헤더의 accessHeader의 값 가져옴
                .filter(refreshToken -> refreshToken.startsWith(BEARER)) //Bearer 로 시작하면 통과
                .map(refreshToken -> refreshToken.replace(BEARER, "")); //'Bearer '부분을 삭제해 순수 토큰만 가져옴
    }

    //클라이언트 요청으로 refresh 토큰 header에서 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (refreshHeader.equals(cookie.getName())) {
                return Optional.ofNullable(cookie.getValue());
            }
        }
        return Optional.empty();
    }


    //AccessToken에서 Email추출
    public Optional<String> extractEmail(String token) {
        try {
            //require을 통해 secretKey를 사용하여 HMAC512알고리즘으로 토큰 유효성 검사 설정
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(token) //access토큰을 검증하고 유효하지 않으면 예외 발생
                    .getClaim(EMAIL_CLAIM) // 이메일 가져옴
                    .asString()); //String 형식으로 가져옴
        } catch (Exception e) {
            log.error("Token이 유효하지 않습니다.");
            return Optional.empty(); //빈 Optional객체 반환
        }
    }
}
