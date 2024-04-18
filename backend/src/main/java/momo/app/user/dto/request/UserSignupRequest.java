package momo.app.user.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UserSignupRequest(
        String nickname,
        MultipartFile image
) {

    public static UserSignupRequest of(UserSignupJsonRequest request, MultipartFile image) {
        return new UserSignupRequest(request.nickname(), image);
    }
}
