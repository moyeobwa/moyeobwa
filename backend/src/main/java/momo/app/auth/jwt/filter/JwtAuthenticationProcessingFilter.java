package momo.app.auth.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.auth.dto.AuthUser;
import momo.app.auth.jwt.service.JwtCreateAndUpdateService;
import momo.app.auth.jwt.service.JwtExtractService;
import momo.app.auth.jwt.service.JwtSendService;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 * 다음 3가지 경우 발생
 * 1. Access 토큰이 유효한 경우 -> Refresh 토큰이 사용자 요청 Header에 없음 -> 인증 성공
 * 2. Access 토큰이 유효하지 않아 사용자 요청 Header에 Refresh 토큰이 있음 -> DB에 Refresh 토큰과 비교하여 일치하면 Access 토큰 재발급 -> 인증 실패로 처리
 * 3. Access 토큰이 없거나 유효하지 않고, Refresh 토큰도 없거나 유효하지 않음 경우 -> 인증 실패, 403 ERROR
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login"; // '/login'으로 들어오는 요청은 Filter사용 X

    private final JwtCreateAndUpdateService jwtCreateAndUpdateService;
    private final JwtExtractService jwtExtractService;
    private final JwtSendService jwtSendService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) { // '/login'으으로 요청이 들어오면 다음 필터를 호출
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtExtractService.extractRefreshToken(request) //사용자 요청 header에서 refresh 토큰 추출
                .filter(jwtCreateAndUpdateService::isTokenValid) //토큰 유효성 검사
                .orElse(null); //토큰이 없거나 유효하지 않으면 null 반환

        // refresh토큰이 사용자 header에 존재, access 토큰이 없어서 refresh 토큰을 요청한 경우
        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken); //refresh 토큰이 DB의 refresh토큰과 일치하는 경우 access 토큰 재발급
            return;
        }

        // refresh토큰이 사용자 header에 존재 X, access 토큰이 있는지, 유효한지 검사
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    //refresh 토큰으로 DB에서 유저를 찾고, 유저가 있다면 access, refresh 토큰 재발급 후 DB 업데이트
    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken) //refresh 토큰으로 user 찾음
                .ifPresent(user -> { // user가 존재하면
                    String reIssueRefreshToken = reIssueRefreshToken(user); // refresh 토큰 재발급, DB 업데이트
                    jwtSendService.sendAccessAndRefreshToken(response, jwtCreateAndUpdateService.createAccessToken(user.getEmail()), reIssueRefreshToken);
                    //header에 access 토큰과 refresh 토큰 담아 보냄
                });
    }

    //refresh 토큰 재발급 후 refresh 토큰 DB에 업데이트
    private String reIssueRefreshToken(User user) {
        String reIssuedRefreshToken = jwtCreateAndUpdateService.createRefreshToken(user.getEmail());
        updateUserRefreshToken(user, reIssuedRefreshToken);
        return reIssuedRefreshToken;
    }

    //user의 refresh 토큰 업데이트 후 저장
    private void updateUserRefreshToken(User user, String reIssuedRefreshToken) {
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
    }


    // access 토큰 유효성 검사 및 인증 처리
    private void checkAccessTokenAndAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )  throws ServletException, IOException{
        jwtExtractService.extractAccessToken(request) //사용자 요청 header에서 access 토큰 추출
                .filter(jwtCreateAndUpdateService::isTokenValid) // 토큰 유효성 검사
                .ifPresent(accessToken -> jwtExtractService.extractEmail(accessToken) // 유효한 토큰이 있으면 이메일 추출
                        .ifPresent(email -> userRepository.findByEmail(email) // 추출된 이메일이 존재하면 이메일로 user 검색
                                .ifPresent(this::saveAuthentication))); // 인증 허가 메소드 실행
        filterChain.doFilter(request, response); //다음 필터로 넘어가도록 설정
    }

    //Security를 사용하여 사용자를 인증 및 허가
    private void saveAuthentication(User user) {
        AuthUser authUser = AuthUser.createAuthUser(user);

        //UserDetails 객체를 사용하여 사용자의 인증 정보를 나타내는 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authUser, null, authoritiesMapper.mapAuthorities(authUser.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authentication); //SecurityContextHolder에 생성된 인증 객체를 설정하여 사용자의 인증 정보를 저장
    }

}
