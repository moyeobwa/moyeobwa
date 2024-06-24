package momo.app.vote.domain;

import java.util.List;
import momo.app.gathering.domain.Gathering;
import momo.app.vote.infrastructure.VoteRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepository extends JpaRepository<Vote, Long>, VoteRepositoryCustom {
}
