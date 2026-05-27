package com.f1gp.f1_quality_gate.model.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReservationStatusTest {

    @Test
    void shouldContainExpectedValues() {
        assertThat(ReservationStatus.values())
                .containsExactly(ReservationStatus.CONFIRMED, ReservationStatus.CANCELLED);
    }
}
