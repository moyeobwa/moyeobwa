package momo.app.auth.oauth2;

import lombok.Getter;
import momo.app.user.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
//추가 정보를 입력하여 회원가입하기 위해서 추가 필드를 가지기 위해 OAuth2User 객체를 상속받아 custom
public class CustomOAuth2User extends DefaultOAuth2User {
    private String email; // 회원가입 전 로그인만 했을 때 토큰을 발급받기 위한 변수
    private Role role; // 회원가입을 한 상태와 안한 상태를 구분하기 위한 role

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
    }
}