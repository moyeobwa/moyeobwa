package momo.app.user.domain;

import jakarta.persistence.*;
import lombok.*;
import momo.app.common.domain.BaseTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class User extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    private String name;

    private String nickname;

    private String email;

    private String imageUrl;

    private String refreshToken;

    // 최초 로그인 구분, GUEST, USER
    @Enumerated(EnumType.STRING)
    private Role role;

    private String state;

    private String description;

    private String interest;

    public void authorizeUser() {
        this.role = Role.USER;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void signUp(String nickname, String imageUrl) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

}

