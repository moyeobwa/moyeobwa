package momo.app.application.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum ApplicationStatus {
    PENDING("PENDING", "대기"),
    APPROVED("APPROVED", "승인"),
    REJECTED("REJECTED", "거절");

    private String key;
    private String value;
}
