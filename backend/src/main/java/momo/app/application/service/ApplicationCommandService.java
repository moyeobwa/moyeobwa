package momo.app.application.service;

import static momo.app.gathering.exception.GatheringErrorCode.GATHERING_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import momo.app.application.domain.Application;
import momo.app.application.domain.ApplicationRepository;
import momo.app.application.dto.ApplicationCreateRequest;
import momo.app.auth.dto.AuthUser;
import momo.app.common.error.exception.BusinessException;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationCommandService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;

    public Long createApplication(AuthUser authUser, ApplicationCreateRequest request) {
        User user = findUser(authUser);
        Gathering gathering = findGathering(request);
        // TODO: 모임 정원 검증
        return applicationRepository.save(Application.builder()
                .user(user)
                .gathering(gathering)
                .build())
                .getId();
    }

    private Gathering findGathering(ApplicationCreateRequest request) {
        return gatheringRepository.findById(request.gatheringId())
                .orElseThrow(() -> new BusinessException(GATHERING_NOT_FOUND));
    }

    private User findUser(AuthUser authUser) {
        return userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

}
