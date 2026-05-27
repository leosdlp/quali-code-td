package com.f1gp.f1_quality_gate.dto.reservation;

import com.f1gp.f1_quality_gate.model.enums.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;

public record ReservationResponse(
        Long id,
        Long spectatorId,
        Long grandstandId,
        List<Long> sessionIds,
        int seatCount,
        double totalPrice,
        ReservationStatus status,
        LocalDateTime bookedAt,
        LocalDateTime cancelledAt,
        double refundedAmount
) {
}
