package com.f1gp.f1_quality_gate.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class RefundService {

    public double calculateRefundRate(LocalDate firstSessionDate, LocalDate cancellationDate) {
        long daysUntilFirstSession = calculateDaysUntilFirstSession(firstSessionDate, cancellationDate);
        return daysUntilFirstSession > 7 ? 1.0 : 0.0;
    }

    public double calculateRefundAmount(double totalPrice, double refundRate) {
        return Math.round(totalPrice * refundRate * 100.0) / 100.0;
    }

    public long calculateDaysUntilFirstSession(LocalDate firstSessionDate, LocalDate cancellationDate) {
        return ChronoUnit.DAYS.between(cancellationDate, firstSessionDate);
    }
}
