package momo.app.gathering.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GatheringSortType {
    LATEST("LATEST", "최신순"),
    MEMBER_COUNT("MEMBER_COUNT", "모임원순");

    private String key;
    private String value;
}
