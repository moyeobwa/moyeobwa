package momo.app.vote.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import momo.app.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum OptionErrorCode implements ErrorCode {

    OPTION_NOT_FOUND("10001", HttpStatus.NOT_FOUND, "옵션을 찾을 수 없습니다."),
    TOO_FEW_OPTIONS("10002", HttpStatus.BAD_REQUEST, "옵션의 개수가 너무 적습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
