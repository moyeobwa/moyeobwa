package momo.app.vote.dto;

import java.time.LocalDateTime;
import momo.app.vote.domain.Vote;

public record VoteResponse(
        String title,
        Long id,
        LocalDateTime createdAt,
        String creatorName
        // TODO: 참여자수, 최고 득표 옵션 등 화면 정해지면 추가하기.
) {
}
