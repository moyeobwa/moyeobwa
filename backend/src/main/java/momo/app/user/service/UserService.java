package momo.app.user.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.auth.dto.AuthUser;
import momo.app.auth.jwt.service.JwtCreateAndUpdateService;
import momo.app.auth.jwt.service.JwtSendService;
import momo.app.image.S3Service;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import momo.app.user.dto.request.UserSignupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final JwtCreateAndUpdateService jwtCreateAndUpdateService;
    private final JwtSendService jwtSendService;

    @Transactional
    public void signUp(
            UserSignupRequest userSignupRequest,
            HttpServletResponse response,
            AuthUser authUser) {

        User user = findUser(authUser);
        String accessToken = jwtCreateAndUpdateService.createAccessToken(user.getEmail());
        String refreshToken = jwtCreateAndUpdateService.createRefreshToken();
        jwtSendService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        user.authorizeUser();
        user.updateRefreshToken(refreshToken);
        String imageUrl = s3Service.upload(userSignupRequest.image());
        user.signUp(userSignupRequest.nickname(), imageUrl);
    }

    private User findUser(AuthUser authUser) {
        return userRepository.findById(authUser.getId())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
    }
}
