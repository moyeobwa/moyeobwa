package momo.app.vote.domain;

import java.util.List;
import momo.app.vote.infrastructure.OptionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OptionRepository extends JpaRepository<Option, Long>, OptionRepositoryCustom {

    @Query("select op from Option op where op.vote = :vote order by op.id desc")
    List<Option> findByVote(Vote vote);
}
