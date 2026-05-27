package com.f1gp.f1_quality_gate.dto.spectator;

import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SpectatorResponse(
        Long id,
        String name,
        String email,
        LocalDate birthDate,
        LoyaltyTier loyaltyTier,
        LocalDateTime createdAt
) {
}
