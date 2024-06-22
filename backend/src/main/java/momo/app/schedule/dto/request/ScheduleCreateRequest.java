package momo.app.schedule.dto.request;

import momo.app.schedule.domain.Color;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleCreateRequest(
        Color color,
        String title,
        String content,
        LocalDate date,
        LocalTime time
) {
}
