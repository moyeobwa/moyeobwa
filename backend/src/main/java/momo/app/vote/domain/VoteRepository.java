package momo.app.vote.domain;

import java.util.List;
import momo.app.gathering.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select v from Vote v where v.gathering = :gathering order by v.id desc")
    List<Vote> findByGathering(Gathering gathering);
}
