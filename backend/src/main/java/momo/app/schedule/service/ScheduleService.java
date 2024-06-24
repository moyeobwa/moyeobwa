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
import momo.app.schedule.dto.request.ScheduleUpdateRequest;
import momo.app.schedule.dto.response.ScheduleResponse;
import momo.app.schedule.exception.ScheduleErrorCode;
import momo.app.user.domain.User;
import momo.app.user.domain.UserRepository;
import momo.app.user.exception.UserErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static momo.app.gathering.exception.GatheringErrorCode.GATHERING_NOT_FOUND;
import static momo.app.gathering.exception.GatheringErrorCode.USER_NOT_IN_GATHERING;
import static momo.app.schedule.exception.ScheduleErrorCode.SCHEDULE_NOT_FOUND;
import static momo.app.user.exception.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;
    private final ScheduleRepository scheduleRepository;

    public Long create(
            ScheduleCreateRequest request,
            AuthUser authUser
    ) {
        User user = findUser(authUser);
        Gathering gathering = findGathering(request.gatheringId());

        validateUserInGathering(request.gatheringId(), user);

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

    public void update(
            Long id,
            ScheduleUpdateRequest request,
            AuthUser authUser
    ) {
        User user = findUser(authUser);
        Schedule schedule = findSchedule(id);

        schedule.validateUserAndSchedule(user);

        schedule.update(request);
    }

    public List<ScheduleResponse> get(Long gatheringId, LocalDate date) {
        Gathering gathering = findGathering(gatheringId);
        List<Schedule> schedules = scheduleRepository.findAllByGatheringIdAndDate(gathering, date);

        return schedules.stream()
                .map(schedule -> ScheduleResponse.from(schedule))
                .toList();

    }

    public void delete(Long id, AuthUser authUser) {
        User user = findUser(authUser);
        Schedule schedule = findSchedule(id);

        schedule.validateUserAndSchedule(user);

        scheduleRepository.delete(schedule);
    }

    private User findUser(AuthUser authUser) {
        return userRepository.findById(authUser.getId())
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

    private Gathering findGathering(Long id) {
        return gatheringRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GATHERING_NOT_FOUND));
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(SCHEDULE_NOT_FOUND));
    }

    private void validateUserInGathering(Long gatheringId, User user) {
        if (!gatheringRepository.checkExistsByGatheringIdAndUserId(gatheringId, user.getId())) {
            throw new BusinessException(USER_NOT_IN_GATHERING);
        }
    }
}
