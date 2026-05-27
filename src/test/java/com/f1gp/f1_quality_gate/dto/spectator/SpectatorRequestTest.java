package com.f1gp.f1_quality_gate.dto.spectator;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class SpectatorRequestTest {

    @Test
    void shouldCreateSpectatorRequest() {
        LocalDate birthDate = LocalDate.of(1990, 4, 12);

        SpectatorRequest request = new SpectatorRequest(
                "Alice Martin",
                "alice@example.com",
                birthDate,
                LoyaltyTier.SILVER
        );

        assertThat(request.name()).isEqualTo("Alice Martin");
        assertThat(request.email()).isEqualTo("alice@example.com");
        assertThat(request.birthDate()).isEqualTo(birthDate);
        assertThat(request.loyaltyTier()).isEqualTo(LoyaltyTier.SILVER);
    }
}
