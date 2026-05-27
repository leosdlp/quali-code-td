package com.f1gp.f1_quality_gate.dto.spectator;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class SpectatorResponseTest {

    @Test
    void shouldCreateSpectatorResponse() {
        LocalDate birthDate = LocalDate.of(1990, 4, 12);
        LocalDateTime createdAt = LocalDateTime.of(2026, 5, 27, 12, 0);

        SpectatorResponse response = new SpectatorResponse(
                1L,
                "Alice Martin",
                "alice@example.com",
                birthDate,
                LoyaltyTier.SILVER,
                createdAt
        );

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Alice Martin");
        assertThat(response.email()).isEqualTo("alice@example.com");
        assertThat(response.birthDate()).isEqualTo(birthDate);
        assertThat(response.loyaltyTier()).isEqualTo(LoyaltyTier.SILVER);
        assertThat(response.createdAt()).isEqualTo(createdAt);
    }
}
