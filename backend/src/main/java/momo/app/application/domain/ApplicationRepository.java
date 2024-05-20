package momo.app.application.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("select ap from Application ap join fetch Gathering g")
    Optional<Application> findByIdWithGathering(Long id);

}
