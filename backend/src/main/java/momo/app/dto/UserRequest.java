package momo.app.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import momo.app.constant.Role;
@Getter
@Setter
public class UserRequest {
    private Long id;

    private String socialId;

    private String name;

    private String email;

    private String image_url;

    // 최초 로그인 구분, GUEST, USER
    @Enumerated(EnumType.STRING)
    private Role role;


}
