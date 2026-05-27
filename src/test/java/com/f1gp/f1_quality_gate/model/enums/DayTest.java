package com.f1gp.f1_quality_gate.model.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DayTest {

    @Test
    void shouldContainExpectedValues() {
        assertThat(Day.values())
                .containsExactly(Day.FRIDAY, Day.SATURDAY, Day.SUNDAY);
    }
}
