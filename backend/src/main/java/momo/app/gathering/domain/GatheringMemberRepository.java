package momo.app.gathering.domain;

import momo.app.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GatheringMemberRepository extends JpaRepository<GatheringMember, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from GatheringMember gm where gm.gathering = :gathering")
    void deleteAllByGathering(Gathering gathering);

    @Query("select gm from GatheringMember gm where gm.user = :user")
    List<GatheringMember> findAllByUser(User user);
}
