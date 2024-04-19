package momo.app.gathering.domain;

import jakarta.persistence.Column;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 45)
    private String name;

    @Builder
    public Tag(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "tag")
    private List<GatheringTag> gatheringTags = new ArrayList<>();

    public void addGatheringTag(GatheringTag gatheringTag) {
        gatheringTags.add(gatheringTag);
    }
}
