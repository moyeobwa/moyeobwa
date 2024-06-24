package momo.app.schedule.exception;

import lombok.Getter;
import momo.app.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum ScheduleErrorCode implements ErrorCode {
    SCHEDULE_NOT_FOUND("7001", HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."),
    SCHEDULE_NOT_EQUAL_USER("7002", HttpStatus.BAD_REQUEST, "일정과 사용자 정보가 일치하지 않습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;

    ScheduleErrorCode(
            String code,
            HttpStatus status,
            String message
    ) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
