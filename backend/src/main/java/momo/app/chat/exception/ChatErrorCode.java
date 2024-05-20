package momo.app.chat.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import momo.app.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum ChatErrorCode implements ErrorCode {
    CHAT_ROOM_NOT_FOUND("5001", HttpStatus.BAD_REQUEST, "채팅방이 없습니다."),
    USER_NOT_IN_CHAT_ROOM("5002", HttpStatus.BAD_REQUEST, "채팅방에 없는 유저입니다.");

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
