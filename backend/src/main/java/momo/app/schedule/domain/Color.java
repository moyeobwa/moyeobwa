package momo.app.schedule.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Color {
    PINK("COLOR_PINK"), YELLOW("COLOR_YELLOW"), GREEN("COLOR_GREEN");

    private final String colorType;
}
