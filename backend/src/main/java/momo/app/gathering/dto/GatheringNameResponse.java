package momo.app.gathering.dto;

import momo.app.gathering.domain.Gathering;

public record GatheringNameResponse(
        Long id,
        String name
) {
    public static GatheringNameResponse from(Gathering gathering) {
        return new GatheringNameResponse(gathering.getId(), gathering.getGatheringInfo().getName());
    }
}
