package momo.app.user.dto.response;

import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;

public record UserResponse(
        Long id,
        String nickname
) {
    public static UserResponse from (User user) {
        return new UserResponse(user.getId(), user.getNickname());
    }
}
