package momo.app.gathering.dto;

import java.util.List;
import momo.app.gathering.domain.Category;

public record GatheringUpdateJsonRequest(
        Category category,
        String name,
        String description,
        List<String> tagNames
) {
}
