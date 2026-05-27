package com.f1gp.f1_quality_gate.model.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LoyaltyTierTest {

    @Test
    void shouldContainExpectedValues() {
        assertThat(LoyaltyTier.values())
                .containsExactly(LoyaltyTier.NONE, LoyaltyTier.SILVER, LoyaltyTier.GOLD);
    }
}
