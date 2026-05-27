package com.f1gp.f1_quality_gate.dto.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class QuoteLineItemResponseTest {

    @Test
    void shouldCreateQuoteLineItemResponse() {
        QuoteLineItemResponse response = new QuoteLineItemResponse(1L, "SUNDAY — RACE", 324.0, 648.0);

        assertThat(response.sessionId()).isEqualTo(1L);
        assertThat(response.sessionLabel()).isEqualTo("SUNDAY — RACE");
        assertThat(response.unitPrice()).isEqualTo(324.0);
        assertThat(response.subtotal()).isEqualTo(648.0);
    }
}
