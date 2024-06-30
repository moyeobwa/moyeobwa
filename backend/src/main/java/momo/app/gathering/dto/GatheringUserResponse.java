package momo.app.gathering.dto;

import momo.app.gathering.domain.Gathering;

public record GatheringUserResponse(
        Long id,
        String name
) {
    public static GatheringUserResponse from(Gathering gathering) {
        return new GatheringUserResponse(gathering.getId(), gathering.getGatheringInfo().getName());
    }
}
