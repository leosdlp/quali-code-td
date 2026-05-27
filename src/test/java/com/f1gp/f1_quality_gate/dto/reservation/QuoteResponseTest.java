package com.f1gp.f1_quality_gate.dto.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuoteResponseTest {

    @Test
    void shouldCreateQuoteResponse() {
        QuoteLineItemResponse lineItem = new QuoteLineItemResponse(1L, "SUNDAY — RACE", 324.0, 648.0);

        QuoteResponse response = new QuoteResponse(
                List.of(lineItem),
                648.0,
                0.0,
                false,
                LoyaltyTier.GOLD,
                64.8,
                0.0,
                583.2
        );

        assertThat(response.lineItems()).containsExactly(lineItem);
        assertThat(response.baseTotal()).isEqualTo(648.0);
        assertThat(response.weekendDiscount()).isEqualTo(0.0);
        assertThat(response.isWeekendPass()).isFalse();
        assertThat(response.loyaltyTier()).isEqualTo(LoyaltyTier.GOLD);
        assertThat(response.loyaltyDiscount()).isEqualTo(64.8);
        assertThat(response.youthDiscount()).isEqualTo(0.0);
        assertThat(response.total()).isEqualTo(583.2);
    }
}
