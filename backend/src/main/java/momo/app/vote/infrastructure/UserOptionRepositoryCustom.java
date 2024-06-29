package momo.app.vote.infrastructure;

import momo.app.user.domain.User;
import momo.app.vote.domain.Vote;

public interface UserOptionRepositoryCustom {

    boolean checkByVoteAndUser(Vote vote, User user);
}
