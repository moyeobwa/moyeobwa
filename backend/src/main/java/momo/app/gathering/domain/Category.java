package momo.app.gathering.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Category {
    EXERCISE("EXERCISE", "운동"),
    STUDY("STUDY", "공부");

    private String key;
    private String value;
}
