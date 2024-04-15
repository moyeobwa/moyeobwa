package momo.app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import momo.app.constant.Role;

@Entity
@Getter
@Setter
public class User extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    private String name;

    private String nickname;

    private String email;

    private String imageUrl;

    // 최초 로그인 구분, GUEST, USER
    @Enumerated(EnumType.STRING)
    private Role role;

    private String state;

    private String description;

    private String interest;

}

