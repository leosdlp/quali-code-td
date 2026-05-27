package com.f1gp.f1_quality_gate.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.entity.RaceSession;
import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RaceSessionRepositoryTest {

    @Autowired
    private RaceSessionRepository raceSessionRepository;

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
