package momo.app.schedule.domain;

import momo.app.gathering.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.gathering = :gathering AND s.date = :date")
    List<Schedule> findAllByGatheringAndDate(Gathering gathering, LocalDate date);
}
