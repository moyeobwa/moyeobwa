package momo.app.user.dto.response;

import momo.app.user.domain.User;

public record UserResponse(
        Long id,
        String name,
        String nickname,
        String email,
        String imageUrl
) {
    public static UserResponse from (User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getEmail(),
                user.getImageUrl());
    }
}
