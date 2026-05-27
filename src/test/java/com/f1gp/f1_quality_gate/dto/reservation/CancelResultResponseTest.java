package com.f1gp.f1_quality_gate.dto.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class CancelResultResponseTest {

    @Test
    void shouldCreateCancelResultResponse() {
        ReservationResponse reservation = new ReservationResponse(
                1L,
                2L,
                3L,
                List.of(4L),
                2,
                648.0,
                ReservationStatus.CANCELLED,
                LocalDateTime.of(2026, 5, 27, 12, 0),
                LocalDateTime.of(2026, 5, 28, 12, 0),
                648.0
        );

        CancelResultResponse response = new CancelResultResponse(reservation, 648.0, 1.0, 15);

        assertThat(response.reservation()).isEqualTo(reservation);
        assertThat(response.refund()).isEqualTo(648.0);
        assertThat(response.rate()).isEqualTo(1.0);
        assertThat(response.daysUntilFirstSession()).isEqualTo(15);
    }
}
