package momo.app.application.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("select ap from Application ap join fetch ap.gathering where ap.id = :id")
    Optional<Application> findByIdWithGathering(Long id);

}
