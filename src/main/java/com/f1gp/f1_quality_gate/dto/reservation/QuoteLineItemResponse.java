package com.f1gp.f1_quality_gate.dto.reservation;

public record QuoteLineItemResponse(
        Long sessionId,
        String sessionLabel,
        double unitPrice,
        double subtotal
) {
}
