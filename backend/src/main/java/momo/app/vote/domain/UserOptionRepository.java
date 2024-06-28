package momo.app.vote.domain;

import momo.app.user.domain.User;
import momo.app.vote.infrastructure.UserOptionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserOptionRepository extends JpaRepository<UserOption, Long>, UserOptionRepositoryCustom {

}
