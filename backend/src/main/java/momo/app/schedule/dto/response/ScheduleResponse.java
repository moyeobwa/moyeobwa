package momo.app.schedule.dto.response;

import momo.app.schedule.domain.Color;
import momo.app.schedule.domain.Schedule;

import java.time.LocalTime;

public record ScheduleResponse(
        Long id,
        Color color,
        String nickname,
        String title,
        String content,
        LocalTime time
) {
    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getColor(),
                schedule.getUser().getNickname(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getTime()
                );
    }
}
