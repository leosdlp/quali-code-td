package com.f1gp.f1_quality_gate.repository;

import com.f1gp.f1_quality_gate.model.entity.Grandstand;
import com.f1gp.f1_quality_gate.model.entity.RaceSession;
import com.f1gp.f1_quality_gate.model.entity.Reservation;
import com.f1gp.f1_quality_gate.model.enums.ReservationStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findBySpectatorId(Long spectatorId);

    List<Reservation> findByGrandstandAndSessionsContainingAndStatus(
            Grandstand grandstand,
            RaceSession session,
            ReservationStatus status
    );
}
