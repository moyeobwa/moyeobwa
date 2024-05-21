package momo.app.application.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import momo.app.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum ApplicationErrorCode implements ErrorCode {
    NOT_FOUND_APPLICATION("7001", HttpStatus.BAD_REQUEST, "모임 신청 내역을 찾을 수 없습니다.");

    private String code;
    private HttpStatus status;
    private String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
