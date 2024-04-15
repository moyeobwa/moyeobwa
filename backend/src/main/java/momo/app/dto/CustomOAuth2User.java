package momo.app.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserRequest userRequest;

    public CustomOAuth2User(UserRequest userRequest) {

        this.userRequest = userRequest;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userRequest.getRole().getKey();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return userRequest.getName();
    }

}