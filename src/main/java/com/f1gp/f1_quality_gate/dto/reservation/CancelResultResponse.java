package com.f1gp.f1_quality_gate.dto.reservation;

public record CancelResultResponse(
        ReservationResponse reservation,
        double refund,
        double rate,
        long daysUntilFirstSession
) {
}
