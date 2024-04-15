package momo.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momo.app.domain.User;
import momo.app.dto.UserRequest;
import momo.app.dto.CustomOAuth2User;
import momo.app.dto.GoogleResponse;
import momo.app.dto.OAuth2Response;
import momo.app.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static momo.app.constant.Role.USER;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        }
        else {

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String socialId = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        User existUser = userRepository.findBySocialId(socialId);

        if (existUser == null) {

            User user = new User();
            user.setSocialId(socialId);
            user.setEmail(oAuth2Response.getEmail());
            user.setName(oAuth2Response.getName());
            user.setRole(USER);

            userRepository.save(user);


        }

        UserRequest userReq = new UserRequest();
        userReq.setSocialId(socialId);
        userReq.setName(oAuth2Response.getName());
        userReq.setRole(USER);
        userReq.setEmail(oAuth2Response.getEmail());

        return new CustomOAuth2User(userReq);

    }
}
