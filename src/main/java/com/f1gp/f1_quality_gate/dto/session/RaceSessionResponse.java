package com.f1gp.f1_quality_gate.dto.session;

import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import java.time.LocalDateTime;

public record RaceSessionResponse(
        Long id,
        Day day,
        SessionType type,
        LocalDateTime date,
        double priceMultiplier
) {
}
