package momo.app.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.auth.dto.AuthUser;
import momo.app.auth.jwt.service.JwtCreateAndUpdateService;
import momo.app.auth.jwt.service.JwtExtractService;
import momo.app.auth.jwt.service.JwtSendService;
import momo.app.common.error.exception.BusinessException;
import momo.app.common.util.RedisUtil;
import momo.app.image.S3Service;
import momo.app.user.domain.Role;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import momo.app.user.dto.request.UserSignupRequest;
import momo.app.user.dto.response.UserResponse;
import momo.app.user.exception.UserErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static momo.app.user.domain.QUser.user;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final JwtCreateAndUpdateService jwtCreateAndUpdateService;
    private final JwtExtractService jwtExtractService;
    private final JwtSendService jwtSendService;
    private final RedisUtil redisUtil;

    @Transactional
    public void signUp(
            UserSignupRequest userSignupRequest,
            HttpServletResponse response,
            AuthUser authUser) {
        User user = findUser(authUser);
        String accessToken = jwtCreateAndUpdateService.createAccessToken(user.getEmail());
        String refreshToken = jwtCreateAndUpdateService.createRefreshToken(user.getEmail());
        jwtSendService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        user.authorizeUser();
        user.updateRefreshToken(refreshToken);
        String imageUrl = s3Service.upload(userSignupRequest.image());
        user.signUp(userSignupRequest.nickname(), imageUrl);
    }

    public void logout(String accessToken, AuthUser authUser) {
        User user = findUser(authUser);
        Long expiration = jwtCreateAndUpdateService.getRemainingExpirationTime(accessToken);

        if (expiration > 0) {
            redisUtil.setBlackList(accessToken, "accessToken", expiration);
        }

        user.logout();
    }

    private User findUser(AuthUser authUser) {
        return userRepository.findById(authUser.getId())
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
    }

    public List<UserResponse> search(String nickname) {
        List<User> users = userRepository.findByNicknameContaining(nickname);
        return users.stream()
                .map(user -> UserResponse.from(user))
                .toList();
    }
}
