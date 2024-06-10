package momo.app.vote.infrastructure;

public interface OptionRepositoryCustom {

    boolean checkExistsByIdAndVoteId(Long id, Long voteId);
}
