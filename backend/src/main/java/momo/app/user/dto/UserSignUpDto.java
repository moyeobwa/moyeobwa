package momo.app.user.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserSignUpDto(
        MultipartFile profile,
        String nickname
) {
}
