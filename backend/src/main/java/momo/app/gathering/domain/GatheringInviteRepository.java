package momo.app.gathering.domain;

import momo.app.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GatheringInviteRepository extends JpaRepository<GatheringInvite, Long> {
    @Query("select gi from GatheringInvite gi where gi.fromUser = :fromUser and gi.toUser = :toUser")
    List<GatheringInvite> findByTwoUser(User fromUser, User toUser);
}
