package momo.app.gathering.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Category {
    EXERCISE("EXERCISE", "운동"),
    STUDY("STUDY", "공부"),
    GAME("GAME", "게임"),
    FOOD("FOOD", "음식"),
    TRAVEL("TRAVEL", "여행");

    private String key;
    private String value;
}
