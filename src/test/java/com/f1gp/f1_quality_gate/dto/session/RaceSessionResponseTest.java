package com.f1gp.f1_quality_gate.dto.session;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class RaceSessionResponseTest {

    @Test
    void shouldCreateRaceSessionResponse() {
        LocalDateTime date = LocalDateTime.of(2026, 7, 19, 14, 0);

        RaceSessionResponse response = new RaceSessionResponse(
                1L,
                Day.SUNDAY,
                SessionType.RACE,
                date,
                1.8
        );

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.day()).isEqualTo(Day.SUNDAY);
        assertThat(response.type()).isEqualTo(SessionType.RACE);
        assertThat(response.date()).isEqualTo(date);
        assertThat(response.priceMultiplier()).isEqualTo(1.8);
    }
}
