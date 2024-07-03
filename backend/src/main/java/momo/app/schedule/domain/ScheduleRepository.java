package momo.app.schedule.domain;

import java.util.Optional;
import momo.app.gathering.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.gathering = :gathering")
    List<Schedule> findAllByGathering(Gathering gathering);

    @Query("SELECT s FROM Schedule s JOIN FETCH s.gathering g WHERE s.id = :id")
    Optional<Schedule> findByIdWithGathering(Long id);

}
