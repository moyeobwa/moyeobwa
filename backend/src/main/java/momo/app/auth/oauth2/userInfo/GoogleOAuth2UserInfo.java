package momo.app.auth.oauth2.userInfo;

import java.util.Map;

//구글에서 가져올 user 정보
public class GoogleOAuth2UserInfo {
    private Map<String, Object> attributes;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return (String) attributes.get("sub");
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public String getEmail() { return (String) attributes.get("email");}
}
