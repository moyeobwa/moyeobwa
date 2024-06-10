package momo.app.auth.exception;

import lombok.Getter;
import momo.app.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum TokenErrorCode implements ErrorCode {
    TOKEN_NOT_FOUND("1001", HttpStatus.NOT_FOUND,"토큰을 찾을 수 없습니다."),
    EMAIL_NOT_FOUND("2001", HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;

    TokenErrorCode(
            String code,
            HttpStatus status,
            String message
    ) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
