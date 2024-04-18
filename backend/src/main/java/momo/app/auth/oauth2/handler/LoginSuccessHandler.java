package momo.app.auth.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.auth.jwt.service.JwtCreateAndUpdateService;
import momo.app.auth.jwt.service.JwtSendService;
import momo.app.auth.oauth2.CustomOAuth2User;
import momo.app.user.domain.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//로그인 성공 시 실행되는 핸들러
@RequiredArgsConstructor
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtCreateAndUpdateService jwtCreateAndUpdateService;
    private final JwtSendService jwtSendService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {
        log.info("OAuth2 Login 성공");

        try {
            CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal(); //소셜로부터 유저 정보 받아옴

            //첫 로그인인 경우 (role이 GUEST인 경우) 회원가입 실행
            if (user.getRole() == Role.GUEST) {
                String accessToken = jwtCreateAndUpdateService.createAccessToken(user.getEmail()); // access 토큰 생성
                jwtSendService.sendAccessAndRefreshToken(response, "Bearer " + accessToken, null); //Header를 통해 두 토큰 보냄

                //회원가입 후 role을 User로 변경
            } else {
                loginSuccess(response, user); //role이 USER인 경우 로그인 실행
            }
        } catch (Exception e) {
            throw e;
        }
    }

    //로그인 실행, access 토큰과 refresh 토큰 생성하여 header를 통해 보냄, DB에 refresh 토큰 업데이트
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User user) throws IOException{
        String accessToken = jwtCreateAndUpdateService.createAccessToken(user.getEmail());
        String refreshToken = jwtCreateAndUpdateService.createRefreshToken();

        jwtSendService.sendAccessAndRefreshToken(response, "Bearer " + accessToken, "Bearer " + refreshToken); //Header를 통해 두 토큰 보냄
        jwtCreateAndUpdateService.updateRefreshToken(user.getEmail(), refreshToken); //사용자 DB의 refresh 토큰 업데이트
    }
}
