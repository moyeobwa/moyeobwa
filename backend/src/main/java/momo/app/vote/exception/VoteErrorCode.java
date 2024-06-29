package momo.app.vote.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import momo.app.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum VoteErrorCode implements ErrorCode {

    VOTE_NOT_FOUND("9000", HttpStatus.NOT_FOUND, "투표를 찾을 수 없습니다."),
    ALREADY_VOTE_END("9001", HttpStatus.BAD_REQUEST, "이미 종료된 투표입니다."),
    ALREADY_VOTE("9002", HttpStatus.BAD_REQUEST, "이미 투표했습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
