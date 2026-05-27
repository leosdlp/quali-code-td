package com.f1gp.f1_quality_gate.model.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class SpectatorTest {

    @Test
    void shouldCreateSpectatorWithNoArgsConstructor() {
        Spectator spectator = new Spectator();

        assertThat(spectator).isNotNull();
    }

    @Test
    void shouldCreateSpectatorWithAllArgsConstructor() {
        LocalDate birthDate = LocalDate.of(1990, 4, 12);
        LocalDateTime createdAt = LocalDateTime.of(2026, 5, 27, 12, 0);

        Spectator spectator = new Spectator(
                1L,
                "Alice Martin",
                "alice@example.com",
                birthDate,
                LoyaltyTier.SILVER,
                createdAt
        );

        assertThat(spectator.getId()).isEqualTo(1L);
        assertThat(spectator.getName()).isEqualTo("Alice Martin");
        assertThat(spectator.getEmail()).isEqualTo("alice@example.com");
        assertThat(spectator.getBirthDate()).isEqualTo(birthDate);
        assertThat(spectator.getLoyaltyTier()).isEqualTo(LoyaltyTier.SILVER);
        assertThat(spectator.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldUpdateSpectatorWithSetters() {
        LocalDate birthDate = LocalDate.of(2000, 1, 15);
        LocalDateTime createdAt = LocalDateTime.of(2026, 5, 27, 13, 0);

        Spectator spectator = new Spectator();

        spectator.setId(2L);
        spectator.setName("Bob Dupont");
        spectator.setEmail("bob@example.com");
        spectator.setBirthDate(birthDate);
        spectator.setLoyaltyTier(LoyaltyTier.GOLD);
        spectator.setCreatedAt(createdAt);

        assertThat(spectator.getId()).isEqualTo(2L);
        assertThat(spectator.getName()).isEqualTo("Bob Dupont");
        assertThat(spectator.getEmail()).isEqualTo("bob@example.com");
        assertThat(spectator.getBirthDate()).isEqualTo(birthDate);
        assertThat(spectator.getLoyaltyTier()).isEqualTo(LoyaltyTier.GOLD);
        assertThat(spectator.getCreatedAt()).isEqualTo(createdAt);
    }
}
