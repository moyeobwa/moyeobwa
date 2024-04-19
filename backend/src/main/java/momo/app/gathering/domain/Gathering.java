package momo.app.gathering.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import momo.app.common.domain.BaseTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gathering extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Category category;

    private String name;

    private String description;

    private String imageUrl;


    @OneToMany(mappedBy = "gathering")
    private List<GatheringTag> gatheringTags = new ArrayList<>();

    @Builder
    public Gathering(
            Category category,
            String name,
            String description,
            String imageUrl,
            String hashTag
    ) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void addGatheringTag(GatheringTag gatheringTag) {
        gatheringTags.add(gatheringTag);
    }

}
