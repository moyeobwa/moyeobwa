package momo.app.user.dto.response;

import momo.app.user.domain.User;

public record UserNicknameResponse(
        Long id,
        String nickname
) {
    public static UserNicknameResponse from (User user) {
        return new UserNicknameResponse(user.getId(), user.getNickname());
    }
}
