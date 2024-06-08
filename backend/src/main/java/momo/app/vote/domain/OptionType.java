package momo.app.vote.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OptionType {

    TEXT("TEXT", "텍스트"),
    DATE("DATE", "날짜");

    private String key;
    private String value;
}
