package com.f1gp.f1_quality_gate.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldCreateResourceNotFoundExceptionWithMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Ressource introuvable");

        assertThat(exception.getMessage()).isEqualTo("Ressource introuvable");
    }
}
