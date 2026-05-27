package com.f1gp.f1_quality_gate.model.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SessionTypeTest {

    @Test
    void shouldContainExpectedValues() {
        assertThat(SessionType.values())
                .containsExactly(SessionType.PRACTICE, SessionType.QUALIFYING, SessionType.SPRINT, SessionType.RACE);
    }
}
