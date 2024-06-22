package momo.app.schedule.service;

import lombok.RequiredArgsConstructor;
import momo.app.auth.dto.AuthUser;
import momo.app.common.error.exception.BusinessException;
import momo.app.gathering.domain.Gathering;
import momo.app.gathering.domain.GatheringRepository;
import momo.app.gathering.exception.GatheringErrorCode;
import momo.app.schedule.domain.Schedule;
import momo.app.schedule.domain.ScheduleRepository;
import momo.app.schedule.dto.request.ScheduleCreateRequest;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import momo.app.user.exception.UserErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;
    private final ScheduleRepository scheduleRepository;

    public Long create(Long id, ScheduleCreateRequest request, AuthUser authUser) {
        User user = findUser(authUser);
        Gathering gathering = findGathering(id);

        Schedule schedule = Schedule.builder()
                .color(request.color())
                .nickname(user.getNickname())
                .title(request.title())
                .content(request.content())
                .date(request.date())
                .time(request.time())
                .gathering(gathering)
                .build();

        scheduleRepository.save(schedule);

        return schedule.getId();
    }

    private User findUser(AuthUser authUser) {
        return userRepository.findById(authUser.getId())
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
    }

    private Gathering findGathering(Long id) {
        return gatheringRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GatheringErrorCode.GATHERING_NOT_FOUND));
    }
}
