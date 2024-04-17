package momo.app.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.auth.dto.AuthUser;
import momo.app.auth.jwt.service.JwtService;
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
    private final JwtService jwtService;

    @Transactional
    public void signUp(
            UserSignupRequest userSignupRequest,
            HttpServletResponse response,
            AuthUser authUser) {
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String email = userDetail.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        */

        User user = findUser(authUser);
        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
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
