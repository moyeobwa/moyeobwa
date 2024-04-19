package momo.app.gathering.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GatheringTagRepository extends JpaRepository<GatheringTag, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from GatheringTag gt where gt.gathering = :gathering")
    void deleteAllByGathering(Gathering gathering);
}
