package momo.app.common.init;

import jakarta.annotation.PostConstruct;
import momo.app.user.domain.Role;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        userRepository.save(User.builder()
                .email("guest")
                .nickname("guest")
                .role(Role.GUEST)
                .socialId("guest")
                .build());
        userRepository.save(User.builder()
                .email("user")
                .nickname("user")
                .role(Role.USER)
                .socialId("user")
                .build());
    }
}
