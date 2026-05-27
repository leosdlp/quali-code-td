package com.f1gp.f1_quality_gate.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class RefundServiceTest {

    private final RefundService refundService = new RefundService();

    @Test
    void calculateRefundRate_shouldReturnFullRefundWhenMoreThanSevenDaysBeforeFirstSession() {
        double rate = refundService.calculateRefundRate(
                LocalDate.of(2026, 7, 19),
                LocalDate.of(2026, 7, 1)
        );

        assertThat(rate).isEqualTo(1.0);
    }

    @Test
    void calculateRefundRate_shouldReturnNoRefundWhenLessThanSevenDaysBeforeFirstSession() {
        double rate = refundService.calculateRefundRate(
                LocalDate.of(2026, 7, 19),
                LocalDate.of(2026, 7, 15)
        );

        assertThat(rate).isEqualTo(0.0);
    }

    @Test
    void calculateRefundAmount_shouldReturnRoundedRefundAmount() {
        double amount = refundService.calculateRefundAmount(933.129, 1.0);

        assertThat(amount).isEqualTo(933.13);
    }

    @Test
    void calculateDaysUntilFirstSession_shouldReturnDifferenceInDays() {
        long days = refundService.calculateDaysUntilFirstSession(
                LocalDate.of(2026, 7, 19),
                LocalDate.of(2026, 7, 4)
        );

        assertThat(days).isEqualTo(15);
    }
}
