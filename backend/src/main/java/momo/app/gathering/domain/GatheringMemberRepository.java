package momo.app.gathering.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GatheringMemberRepository extends JpaRepository<GatheringMember, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from GatheringMember gm where gm.gathering = :gathering")
    void deleteAllByGathering(Gathering gathering);
}
