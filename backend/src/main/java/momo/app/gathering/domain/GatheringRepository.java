package momo.app.gathering.domain;

import momo.app.gathering.infrastructure.GatheringRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GatheringRepository extends JpaRepository<Gathering, Long>, GatheringRepositoryCustom {

    @Modifying(clearAutomatically = true)
    @Query("update Gathering g set g.isDeleted = true where g.id = :id")
    void deleteById(Long id);
}
