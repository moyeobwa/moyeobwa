package momo.app.gathering.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import momo.app.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum GatheringErrorCode implements ErrorCode {
    GATHERING_NOT_FOUND("6001", HttpStatus.BAD_REQUEST, "모임방이 존재하지 않습니다."),
    NOT_MANAGER("6002", HttpStatus.FORBIDDEN, "모임방의 메니저가 아닙니다."),
    USER_NOT_IN_GATHERING("6003", HttpStatus.BAD_REQUEST, "모임방에 존재하지 않는 유저입니다."),
    NOT_TO_USER("6004", HttpStatus.FORBIDDEN, "사용자 정보가 일치하지 않습니다."),
    GATHERING_INVITE_NOT_FOUND("6005", HttpStatus.BAD_REQUEST, "모임 초대 정보가 존재하지 않습니다."),
    GATHERING_INVITE_ALREADY_EXISTS("6006", HttpStatus.CONFLICT, "이미 초대하였습니다.");

    private String code;
    private HttpStatus statue;
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
        return statue;
    }
}
