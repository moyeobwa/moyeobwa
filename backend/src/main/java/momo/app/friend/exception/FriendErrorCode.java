package momo.app.friend.exception;

import lombok.Getter;
import momo.app.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum FriendErrorCode implements ErrorCode {

    FRIEND_REQUEST_ALREADY_EXISTS("3001", HttpStatus.CONFLICT,"이미 친구 요청 상태입니다."),
    FRIEND_ALREADY_EXISTS("3002", HttpStatus.CONFLICT, "이미 친구입니다."),
    FRIEND_NOT_FOUND("3003", HttpStatus.NOT_FOUND, "해당 친구가 존재하지 않습니다."),
    FRIEND_DELETE_PERMISSION_DENIED("3004", HttpStatus.FORBIDDEN, "사용자와 친구 정보가 일치하지 않습니다."),

    FRIEND_REQUEST_SELF("3005", HttpStatus.BAD_REQUEST, "자기 자신에게 친구 신청을 할 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;

    FriendErrorCode(
            String code,
            HttpStatus status,
            String message
    ) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
