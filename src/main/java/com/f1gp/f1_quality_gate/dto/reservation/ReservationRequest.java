package com.f1gp.f1_quality_gate.dto.reservation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ReservationRequest(
        @NotNull Long spectatorId,
        @NotNull Long grandstandId,
        @NotEmpty List<Long> sessionIds,
        @Min(1) @Max(6) int seatCount
) {
}
