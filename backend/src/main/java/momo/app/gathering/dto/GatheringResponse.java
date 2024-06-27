package momo.app.gathering.dto;

import java.time.LocalDateTime;

public record GatheringResponse(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        String imageUrl,
        int numberOfMembers
) {
}
