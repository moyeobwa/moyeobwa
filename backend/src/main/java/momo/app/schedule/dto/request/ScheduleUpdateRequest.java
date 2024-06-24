package momo.app.schedule.dto.request;

import momo.app.schedule.domain.Color;

import java.time.LocalTime;

public record ScheduleUpdateRequest(
        Color color,
        String title,
        String content,
        LocalTime time
) {
}
