package momo.app.auth.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.common.util.RedisUtil;
import momo.app.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtCreateAndUpdateService {
    @Value("${jwt.secretKey}")
    private String secretKey; //jwt 비밀키

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod; // access 토큰 유효 시간

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod; // refresh 토큰 유효 시간

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    //access 토큰 생성
    public String createAccessToken(String email) {
        Date now = new Date(); // 현재 시간

        return JWT.create() // JWT 토큰 생성 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT) //JWT subject를 access 토큰으로 설정
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간을 access 토큰 유효시간으로 설정
                .withClaim(EMAIL_CLAIM, email) // 클레임을 이메일 값으로 설정
                .sign(Algorithm.HMAC512(secretKey)); //HMAC512 알고리즘을 사용하여 secret키로 암호화하여 access 토큰 생성
    }

    //refresh 토큰 생성
    //대부분 access 토큰 생성과정과 동일하지만 클레임에 이메일 설정 X
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512((secretKey)));
    }

    //RefreshToken을 DB에 업데이트
    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken), //회원이 존재하면 refresh 토큰 업데이트
                        () -> new IllegalStateException("일치하는 회원이 없습니다.") //회원이 존재하지 않으면 exception 발생
                );
    }

    public Long getRemainingExpirationTime(String accessToken) {
        Long expiration = JWT.decode(accessToken).getExpiresAt().getTime();
        Long now = new Date().getTime();

        return (expiration - now);
    }

    //토큰 유효성 검사
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            if (redisUtil.hasKeyBlackList(token)) {
                throw new RuntimeException("토그아웃 상태의 토큰입니다.");
            }
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }
}
