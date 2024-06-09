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
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

//로그인 성공 시 실행되는 핸들러
@RequiredArgsConstructor
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtCreateAndUpdateService jwtCreateAndUpdateService;
    private final JwtSendService jwtSendService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {
        log.info("OAuth2 Login 성공");

        try {
            CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal(); //소셜로부터 유저 정보 받아옴
            String accessToken = jwtCreateAndUpdateService.createAccessToken(user.getEmail()); // access 토큰 생성
            String refreshToken = jwtCreateAndUpdateService.createRefreshToken(user.getEmail());
            jwtSendService.sendAccessAndRefreshToken(response, "Bearer " + accessToken, refreshToken);
            jwtCreateAndUpdateService.updateRefreshToken(user.getEmail(), refreshToken);

            if (user.getRole() == Role.USER) {
                loginSuccess(user, refreshToken);
                response.sendRedirect("https://momo.moyeobwa-dev.shop/");
            } else {
                response.sendRedirect("https://momo.moyeobwa-dev.shop/sign-up");
            }


        } catch (Exception e) {
            throw e;
        }
    }

    //로그인 실행, access 토큰과 refresh 토큰 생성하여 header를 통해 보냄, DB에 refresh 토큰 업데이트
    private void loginSuccess(CustomOAuth2User user, String refreshToken) throws IOException{
        jwtCreateAndUpdateService.updateRefreshToken(user.getEmail(), refreshToken); //사용자 DB의 refresh 토큰 업데이트
    }
}
