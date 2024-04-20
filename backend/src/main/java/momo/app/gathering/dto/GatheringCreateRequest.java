package momo.app.gathering.dto;

import java.util.List;
import momo.app.gathering.domain.Category;
import org.springframework.web.multipart.MultipartFile;

public record GatheringCreateRequest(
    Category category,
    String name,
    String description,
    List<String> tagNames,
    MultipartFile image
) {

    public static GatheringCreateRequest of(GatheringCreateJsonRequest request, MultipartFile image) {
        return new GatheringCreateRequest(
                request.category(),
                request.name(),
                request.description(),
                request.tagNames(),
                image
        );
    }
}
