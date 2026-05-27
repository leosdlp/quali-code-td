package com.f1gp.f1_quality_gate.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.f1gp.f1_quality_gate.model.entity.Grandstand;
import com.f1gp.f1_quality_gate.model.entity.RaceSession;
import com.f1gp.f1_quality_gate.model.entity.Reservation;
import com.f1gp.f1_quality_gate.model.entity.Spectator;
import com.f1gp.f1_quality_gate.model.enums.Category;
import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import com.f1gp.f1_quality_gate.model.enums.ReservationStatus;
import com.f1gp.f1_quality_gate.model.enums.SessionType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private GrandstandRepository grandstandRepository;

    @Autowired
    private RaceSessionRepository raceSessionRepository;

    @Autowired
    private SpectatorRepository spectatorRepository;

    @BeforeEach
    void cleanDatabase() {
        reservationRepository.deleteAll();
        spectatorRepository.deleteAll();
        grandstandRepository.deleteAll();
        raceSessionRepository.deleteAll();
    }

    @Test
    void findBySpectatorId_shouldReturnMatchingReservations() {
        Spectator spectator = spectatorRepository.save(createSpectator("alice@example.com"));
        Grandstand grandstand = grandstandRepository.save(createGrandstand());
        RaceSession raceSession = raceSessionRepository.save(createRaceSession());

        Reservation reservation = createReservation(spectator, grandstand, raceSession);
        reservationRepository.save(reservation);

        assertThat(reservationRepository.findBySpectatorId(spectator.getId()))
                .hasSize(1)
                .first()
                .extracting(Reservation::getSpectator)
                .isEqualTo(spectator);
    }

    @Test
    void findByGrandstandAndSessionsContainingAndStatus_shouldReturnMatchingReservations() {
        Spectator spectator = spectatorRepository.save(createSpectator("bob@example.com"));
        Grandstand grandstand = grandstandRepository.save(createGrandstand());
        RaceSession raceSession = raceSessionRepository.save(createRaceSession());

        Reservation reservation = createReservation(spectator, grandstand, raceSession);
        reservationRepository.save(reservation);

        assertThat(reservationRepository.findByGrandstandAndSessionsContainingAndStatus(
                grandstand,
                raceSession,
                ReservationStatus.CONFIRMED
        )).hasSize(1);
    }

    private Spectator createSpectator(String email) {
        return new Spectator(
                null,
                "Test Spectator",
                email,
                LocalDate.of(1990, 4, 12),
                LoyaltyTier.SILVER,
                LocalDateTime.now()
        );
    }

    private Grandstand createGrandstand() {
        return new Grandstand(
                null,
                "Tribune Sainte-Beaume",
                "Virage 3",
                Category.GOLD,
                200,
                180.0,
                true
        );
    }

    private RaceSession createRaceSession() {
        return new RaceSession(
                null,
                Day.SUNDAY,
                SessionType.RACE,
                LocalDateTime.of(2026, 7, 19, 14, 0),
                1.8
        );
    }

    private Reservation createReservation(Spectator spectator, Grandstand grandstand, RaceSession raceSession) {
        return new Reservation(
                null,
                spectator,
                grandstand,
                List.of(raceSession),
                2,
                648.0,
                ReservationStatus.CONFIRMED,
                LocalDateTime.now(),
                null,
                0.0
        );
    }
}
