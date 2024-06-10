package momo.app.user.exception;

import lombok.Getter;
import momo.app.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("2001", HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;

    UserErrorCode(
            String code,
            HttpStatus status,
            String message
    ) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
