package com.f1gp.f1_quality_gate.model.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class RaceSessionTest {

    @Test
    void shouldCreateRaceSessionWithNoArgsConstructor() {
        RaceSession raceSession = new RaceSession();

        assertThat(raceSession).isNotNull();
    }

    @Test
    void shouldCreateRaceSessionWithAllArgsConstructor() {
        LocalDateTime date = LocalDateTime.of(2026, 7, 19, 14, 0);

        RaceSession raceSession = new RaceSession(
                1L,
                Day.SUNDAY,
                SessionType.RACE,
                date,
                1.8
        );

        assertThat(raceSession.getId()).isEqualTo(1L);
        assertThat(raceSession.getDay()).isEqualTo(Day.SUNDAY);
        assertThat(raceSession.getType()).isEqualTo(SessionType.RACE);
        assertThat(raceSession.getDate()).isEqualTo(date);
        assertThat(raceSession.getPriceMultiplier()).isEqualTo(1.8);
    }

    @Test
    void shouldUpdateRaceSessionWithSetters() {
        LocalDateTime date = LocalDateTime.of(2026, 7, 18, 16, 0);
        RaceSession raceSession = new RaceSession();

        raceSession.setId(2L);
        raceSession.setDay(Day.SATURDAY);
        raceSession.setType(SessionType.QUALIFYING);
        raceSession.setDate(date);
        raceSession.setPriceMultiplier(1.0);

        assertThat(raceSession.getId()).isEqualTo(2L);
        assertThat(raceSession.getDay()).isEqualTo(Day.SATURDAY);
        assertThat(raceSession.getType()).isEqualTo(SessionType.QUALIFYING);
        assertThat(raceSession.getDate()).isEqualTo(date);
        assertThat(raceSession.getPriceMultiplier()).isEqualTo(1.0);
    }
}
