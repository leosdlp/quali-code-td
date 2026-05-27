package com.f1gp.f1_quality_gate.dto.session;

import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RaceSessionRequest(
        @NotNull Day day,
        @NotNull SessionType type,
        @NotNull LocalDateTime date,
        @DecimalMin("0.01") Double priceMultiplier
) {
}
