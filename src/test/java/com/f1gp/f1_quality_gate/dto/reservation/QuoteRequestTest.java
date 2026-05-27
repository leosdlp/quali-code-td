package com.f1gp.f1_quality_gate.dto.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class QuoteRequestTest {

    @Test
    void shouldCreateQuoteRequest() {
        QuoteRequest request = new QuoteRequest(1L, List.of(1L, 2L, 3L), 2, 4L);

        assertThat(request.grandstandId()).isEqualTo(1L);
        assertThat(request.sessionIds()).containsExactly(1L, 2L, 3L);
        assertThat(request.seatCount()).isEqualTo(2);
        assertThat(request.spectatorId()).isEqualTo(4L);
    }
}
