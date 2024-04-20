package momo.app.gathering.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GatheringInfo {

    private static final int NAME_LENGTH = 25;

    @Column(nullable = false)
    private Category category;

    @Column(unique = true, length = 25, nullable = false)
    private String name;

    @Column(length = 100)
    private String description;

    @Column(nullable = false)
    private String imageUrl;


    private GatheringInfo(
            Category category,
            String name,
            String description,
            String imageUrl
    ) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static GatheringInfo of(
            Category category,
            String name,
            String description,
            String imageUrl
    ) {

        validateName(name);
        validateCategory(category);

        return new GatheringInfo(
                category,
                name,
                description,
                imageUrl
        );
    }

    private static void validateName(String name) {
        if (name.isBlank() || name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException("Gathering name must be between 1 and 25 characters.");
        }
    }

    private static void validateCategory(Category category) {
        if (Objects.isNull(category)) {
            throw new IllegalArgumentException("Category is null");
        }
    }

}
