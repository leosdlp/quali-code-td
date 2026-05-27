package com.f1gp.f1_quality_gate.dto.error;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponse() {
        ErrorResponse response = new ErrorResponse("Erreur test");

        assertThat(response.error()).isEqualTo("Erreur test");
    }
}
