package momo.app.user.dto.request;

import momo.app.user.domain.Role;

public record UserRequest(
        Long id,
        String socialId,
        String name,
        String email,
        String image_url,
        Role role
) {
}
