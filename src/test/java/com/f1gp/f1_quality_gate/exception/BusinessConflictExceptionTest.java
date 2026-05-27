package com.f1gp.f1_quality_gate.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BusinessConflictExceptionTest {

    @Test
    void shouldCreateBusinessConflictExceptionWithMessage() {
        BusinessConflictException exception = new BusinessConflictException("Conflit métier");

        assertThat(exception.getMessage()).isEqualTo("Conflit métier");
    }
}
