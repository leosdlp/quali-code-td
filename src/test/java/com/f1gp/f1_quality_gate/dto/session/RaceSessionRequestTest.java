package com.f1gp.f1_quality_gate.dto.session;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class RaceSessionRequestTest {

    @Test
    void shouldCreateRaceSessionRequest() {
        LocalDateTime date = LocalDateTime.of(2026, 7, 19, 14, 0);

        RaceSessionRequest request = new RaceSessionRequest(
                Day.SUNDAY,
                SessionType.RACE,
                date,
                1.8
        );

        assertThat(request.day()).isEqualTo(Day.SUNDAY);
        assertThat(request.type()).isEqualTo(SessionType.RACE);
        assertThat(request.date()).isEqualTo(date);
        assertThat(request.priceMultiplier()).isEqualTo(1.8);
    }
}
