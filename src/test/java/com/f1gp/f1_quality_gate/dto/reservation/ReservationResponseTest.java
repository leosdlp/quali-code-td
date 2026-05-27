package com.f1gp.f1_quality_gate.dto.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReservationResponseTest {

    @Test
    void shouldCreateReservationResponse() {
        LocalDateTime bookedAt = LocalDateTime.of(2026, 5, 27, 12, 0);

        ReservationResponse response = new ReservationResponse(
                1L,
                2L,
                3L,
                List.of(4L, 5L),
                2,
                648.0,
                ReservationStatus.CONFIRMED,
                bookedAt,
                null,
                0.0
        );

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.spectatorId()).isEqualTo(2L);
        assertThat(response.grandstandId()).isEqualTo(3L);
        assertThat(response.sessionIds()).containsExactly(4L, 5L);
        assertThat(response.seatCount()).isEqualTo(2);
        assertThat(response.totalPrice()).isEqualTo(648.0);
        assertThat(response.status()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(response.bookedAt()).isEqualTo(bookedAt);
        assertThat(response.cancelledAt()).isNull();
        assertThat(response.refundedAmount()).isEqualTo(0.0);
    }
}
