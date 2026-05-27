package com.f1gp.f1_quality_gate.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.dto.error.ErrorResponse;
import com.f1gp.f1_quality_gate.exception.BusinessConflictException;
import com.f1gp.f1_quality_gate.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleResourceNotFound_shouldReturnErrorMessage() {
        ErrorResponse response = handler.handleResourceNotFound(
                new ResourceNotFoundException("Tribune introuvable")
        );

        assertThat(response.error()).isEqualTo("Tribune introuvable");
    }

    @Test
    void handleBusinessConflict_shouldReturnErrorMessage() {
        ErrorResponse response = handler.handleBusinessConflict(
                new BusinessConflictException("Email déjà utilisé")
        );

        assertThat(response.error()).isEqualTo("Email déjà utilisé");
    }

    @Test
    void handleBadRequest_shouldReturnGenericErrorMessage() {
        ErrorResponse response = handler.handleBadRequest(new RuntimeException("Erreur"));

        assertThat(response.error()).isEqualTo("Données d'entrée invalides");
    }
}
