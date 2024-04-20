package momo.app.gathering.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import momo.app.auth.dto.AuthUser;
import momo.app.common.domain.BaseTime;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Gathering extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private GatheringInfo gatheringInfo;

    @Column(nullable = false)
    private Long managerId;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "gathering")
    private List<GatheringTag> gatheringTags = new ArrayList<>();

    @Builder
    public Gathering(GatheringInfo gatheringInfo, Long managerId) {
        this.gatheringInfo = gatheringInfo;
        this.managerId = managerId;
    }

    public void addGatheringTag(GatheringTag gatheringTag) {
        gatheringTags.add(gatheringTag);
    }

    public void validateManager(AuthUser authUser) {
        if (authUser.getId() != managerId) {
            throw new IllegalStateException("Gathering can only be modified by the host.");
        }
    }

    public void updateGatheringInfo(GatheringInfo gatheringInfo) {
        this.gatheringInfo = gatheringInfo;
    }

}
