package momo.app.vote.domain;

import momo.app.vote.infrastructure.OptionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long>, OptionRepositoryCustom {
}
