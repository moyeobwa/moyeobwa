package momo.app.auth.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.auth.oauth2.CustomOAuth2User;
import momo.app.auth.oauth2.OAuthAttribute;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

        //DefaultOAuth2UserService의 loadUser 메소드를 통해 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서 사용자 정보를 얻음
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인 API가 제공하는 userInfo의 Json

        OAuthAttribute extractAttribute = OAuthAttribute.of(userNameAttributeName, attributes); // 유저 정보를 이용하여 OAuthAttribute 객체 생성

        User createdUser = getUser(extractAttribute); // OAuthAttribute 객체를 이용하여 생성할 User객체를 가져와 생성할 User 객체 생성

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
                attributes,
                extractAttribute.getNameAttributeKey(),
                createdUser.getEmail(),
                createdUser.getRole()
        );
    }

    //OAuthAttribute로 User를 찾아 가져옴
    private User getUser(OAuthAttribute attribute) {
        User findUser = userRepository.findBySocialId(attribute.getUserInfo().getId()).orElse(null);

        if(findUser == null) {
            return saveUser(attribute); //User가 존재하지 않으면 저장
        }
        return findUser;
    }

    private User saveUser(OAuthAttribute attribute) {
        User createdUser = attribute.toEntity(attribute.getUserInfo());
        return userRepository.save(createdUser);
    }
}
