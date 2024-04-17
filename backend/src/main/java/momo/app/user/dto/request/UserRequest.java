package momo.app.user.dto.request;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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
