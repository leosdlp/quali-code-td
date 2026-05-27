package com.f1gp.f1_quality_gate.dto.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ReservationRequestTest {

    @Test
    void shouldCreateReservationRequest() {
        ReservationRequest request = new ReservationRequest(1L, 2L, List.of(3L, 4L), 2);

        assertThat(request.spectatorId()).isEqualTo(1L);
        assertThat(request.grandstandId()).isEqualTo(2L);
        assertThat(request.sessionIds()).containsExactly(3L, 4L);
        assertThat(request.seatCount()).isEqualTo(2);
    }
}
