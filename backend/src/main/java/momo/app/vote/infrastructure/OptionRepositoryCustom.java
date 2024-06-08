package momo.app.vote.infrastructure;

import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepositoryCustom {

    boolean existsByIdAndVoteId(Long id, Long voteId);
}
