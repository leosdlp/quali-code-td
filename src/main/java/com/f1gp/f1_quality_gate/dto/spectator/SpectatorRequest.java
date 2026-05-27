package com.f1gp.f1_quality_gate.dto.spectator;

import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record SpectatorRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @Past LocalDate birthDate,
        LoyaltyTier loyaltyTier
) {
}
