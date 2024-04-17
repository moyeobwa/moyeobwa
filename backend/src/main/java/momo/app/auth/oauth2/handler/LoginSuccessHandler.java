package momo.app.auth.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.auth.jwt.service.JwtService;
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
    private final JwtService jwtService;

    /*
    저희 컨벤션에 parameter 3개 이상이거나 120자 이상이면 개행하기로 했어요~
     */
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
                String accessToken = jwtService.createAccessToken(user.getEmail()); // access 토큰 생성

                /*
                response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken); //header에 access 토큰 저장
                -> 대현님 작성하신 sendAccessAndRefreshToken 메서드 안에서 header setting 하는 로직이 이미 있어서 이 부분은 빼는 게 좋을 것 같네요
                 */

                /*
                response.sendRedirect("/user/sign-up"); //회원가입 페이지로 redirect
                -> 여기서 회원가입 api 리디렉션하면 Get요청으로 가서 415 에러 발생하고 nickname, image도 전달 못해요!
                */

                //임시 코드, 클래스 분리 or 여기서 처리
                response.setStatus(HttpServletResponse.SC_OK);
                response.setHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
                response.setHeader(jwtService.getRefreshHeader(), null);

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
        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        /*
        -> 여기도 마찬가지인데 헤더 세팅 여기서 했는데 sendAccessAndRefreshToken()에서 중복 세팅하고 있어요.
        재 개인적인 의견으로는 jwtService가 너무 많은 역할을 담당하는 것 같아서 Token 발급만 jwtServide에서 담당하고 헤더 세팅은
        별도의 클래스에서 처리하는게 좋을 것 같아요~
         */
        response.setHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.setHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);
        response.setStatus(HttpServletResponse.SC_OK);

        //jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); //Header를 통해 두 토큰 보냄

        /*
        위와 같은 맥락으로 jwtService에서 토큰 발행, 추출, 응답세팅, 리프레쉬 토큰 재설정 ..등 너무 많은 책임을 가지고 있는 것 같아요.
        다 클래스로 분리하는 게 좋을 것 같네요!
         */
        jwtService.updateRefreshToken(user.getEmail(), refreshToken); //사용자 DB의 refresh 토큰 업데이트
    }
}
