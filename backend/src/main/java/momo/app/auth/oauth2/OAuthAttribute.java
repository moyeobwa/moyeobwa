package momo.app.auth.oauth2;

import lombok.Builder;
import lombok.Getter;
import momo.app.auth.oauth2.userInfo.GoogleOAuth2UserInfo;
import momo.app.user.domain.Role;
import momo.app.user.domain.User;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttribute {
    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값
    private GoogleOAuth2UserInfo userInfo; // 로그인 유저 정보

    @Builder
    public OAuthAttribute(String nameAttributeKey, GoogleOAuth2UserInfo userInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.userInfo = userInfo;
    }

    //OAuthAttribute 빌드 시 유저 정보 필드에 구글 유저 정보를 생성하여 빌드
    public static OAuthAttribute of(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttribute.builder()
                .nameAttributeKey(userNameAttributeName)
                .userInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public User toEntity(GoogleOAuth2UserInfo userInfo) {
        return User.builder()
                .socialId(userInfo.getId())
                .email(UUID.randomUUID() + "@socialUser.com")
                .name(userInfo.getName())
                .imageUrl(userInfo.getImageUrl())
                .role(Role.GUEST)
                .build();
    }



}
