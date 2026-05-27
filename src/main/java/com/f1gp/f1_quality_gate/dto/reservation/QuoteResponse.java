package com.f1gp.f1_quality_gate.dto.reservation;

import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import java.util.List;

public record QuoteResponse(
        List<QuoteLineItemResponse> lineItems,
        double baseTotal,
        double weekendDiscount,
        boolean isWeekendPass,
        LoyaltyTier loyaltyTier,
        double loyaltyDiscount,
        double youthDiscount,
        double total
) {
}
