package com.f1gp.f1_quality_gate.repository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.f1gp.f1_quality_gate.model.entity.RaceSession;
import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.SessionType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RaceSessionRepositoryTest {

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
    void save_shouldPersistRaceSession() {
        RaceSession raceSession = new RaceSession(
                null,
                Day.SUNDAY,
                SessionType.RACE,
                LocalDateTime.of(2026, 7, 19, 14, 0),
                1.8
        );

        RaceSession savedRaceSession = raceSessionRepository.save(raceSession);

        assertThat(savedRaceSession.getId()).isNotNull();
        assertThat(raceSessionRepository.findById(savedRaceSession.getId())).isPresent();
    }
}
