package momo.app.gathering.dto;

import java.time.LocalDateTime;

public record GatheringResponse(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime lastActivityTime,
        String imageUrl,
        int numberOfMembers
) {
}
