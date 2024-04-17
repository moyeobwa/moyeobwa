package momo.app.auth.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {
    @Value("${jwt.secretKey}")
    private String secretKey; //jwt 비밀키

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod; // access 토큰 유효 시간

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod; // refresh 토큰 유효 시간

    @Value("${jwt.access.header}")
    private String accessHeader; // access 헤더

    @Value("${jwt.refresh.header}")
    private String refreshHeader; // refresh 헤더

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";
    private final UserRepository userRepository;

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
        response.setHeader(refreshHeader, refreshToken); //Http 헤더에 refreshHeader를 키로 refresh 토큰 저장
        log.info("Access Token : {}, Refresh Token : {}", accessToken, refreshToken);
    }

    //클라이언트의 요청으로 access 토큰 header에서 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader)) // 헤더의 accessHeader의 값 가져옴
                .filter(refreshToken -> refreshToken.startsWith(BEARER)) //Bearer 로 시작하면 통과
                .map(refreshToken -> refreshToken.replace(BEARER, "")); //'Bearer '부분을 삭제해 순수 토큰만 가져옴
    }

    //클라이언트 요청으로 refresh 토큰 header에서 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader)) // 헤더의 refreshHeader의 값 가져옴
                .filter(refreshToken -> refreshToken.startsWith(BEARER)) //Bearer 로 시작하면 통과
                .map(refreshToken -> refreshToken.replace(BEARER, "")); //'Bearer '부분을 삭제해 순수 토큰만 가져옴
    }

    //AccessToken에서 Email추출
    public Optional<String> extractEmail(String accessToken) {
        try {
            //require을 통해 secretKey를 사용하여 HMAC512알고리즘으로 토큰 유효성 검사 설정
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(accessToken) //access토큰을 검증하고 유효하지 않으면 예외 발생
                    .getClaim(EMAIL_CLAIM) // 이메일 가져옴
                    .asString()); //String 형식으로 가져옴
        } catch (Exception e) {
            log.error("Access Token이 유효하지 않습니다.");
            return Optional.empty(); //빈 Optional객체 반환
        }
    }

    //RefreshToken을 DB에 업데이트
    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken), //회원이 존재하면 refresh 토큰 업데이트
                        () -> new Exception("일치하는 회원이 없습니다.") //회원이 존재하지 않으면 exception 발생
                );
    }

    //토큰 유효성 검사
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }
}
