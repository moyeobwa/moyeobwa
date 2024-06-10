package momo.app.vote.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum VoteStatus {
    PROGRESS("PROGRESS", "진행"),
    END("END", "종료");

    private String key;
    private String value;
}
