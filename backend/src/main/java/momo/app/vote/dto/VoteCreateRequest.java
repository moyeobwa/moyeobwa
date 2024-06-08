package momo.app.vote.dto;

import java.util.List;

public record VoteCreateRequest(
        String title,
        Long gatheringId,
        List<String> optionNames
) {
}
