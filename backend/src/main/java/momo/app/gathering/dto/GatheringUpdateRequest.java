package momo.app.gathering.dto;

import java.util.List;
import momo.app.gathering.domain.Category;
import org.springframework.web.multipart.MultipartFile;

public record GatheringUpdateRequest(
        Category category,
        String name,
        String description,
        List<String> tagNames,
        MultipartFile image
) {

    public static GatheringUpdateRequest of(GatheringUpdateJsonRequest request, MultipartFile image) {
        return new GatheringUpdateRequest(
                request.category(),
                request.name(),
                request.description(),
                request.tagNames(),
                image
        );
    }
}
