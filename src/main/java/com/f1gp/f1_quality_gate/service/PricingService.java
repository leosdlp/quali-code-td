package com.f1gp.f1_quality_gate.service;

import com.f1gp.f1_quality_gate.dto.reservation.QuoteLineItemResponse;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteRequest;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteResponse;
import com.f1gp.f1_quality_gate.exception.ResourceNotFoundException;
import com.f1gp.f1_quality_gate.model.entity.Grandstand;
import com.f1gp.f1_quality_gate.model.entity.RaceSession;
import com.f1gp.f1_quality_gate.model.entity.Spectator;
import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import com.f1gp.f1_quality_gate.repository.GrandstandRepository;
import com.f1gp.f1_quality_gate.repository.RaceSessionRepository;
import com.f1gp.f1_quality_gate.repository.SpectatorRepository;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PricingService {

    private static final double WEEKEND_DISCOUNT_RATE = 0.20;
    private static final double SILVER_DISCOUNT_RATE = 0.05;
    private static final double GOLD_DISCOUNT_RATE = 0.10;
    private static final double YOUTH_DISCOUNT_RATE = 0.50;
    private static final int YOUTH_AGE_LIMIT = 16;

    private final GrandstandRepository grandstandRepository;
    private final RaceSessionRepository raceSessionRepository;
    private final SpectatorRepository spectatorRepository;

    public PricingService(
            GrandstandRepository grandstandRepository,
            RaceSessionRepository raceSessionRepository,
            SpectatorRepository spectatorRepository
    ) {
        this.grandstandRepository = grandstandRepository;
        this.raceSessionRepository = raceSessionRepository;
        this.spectatorRepository = spectatorRepository;
    }

    public QuoteResponse quote(QuoteRequest request) {
        Grandstand grandstand = grandstandRepository.findById(request.grandstandId())
                .orElseThrow(() -> new ResourceNotFoundException("Tribune introuvable"));

        List<RaceSession> sessions = raceSessionRepository.findAllById(request.sessionIds());

        if (sessions.size() != request.sessionIds().size()) {
            throw new ResourceNotFoundException("Session introuvable");
        }

        Spectator spectator = getSpectator(request.spectatorId());

        List<QuoteLineItemResponse> lineItems = sessions.stream()
                .map(session -> toLineItem(grandstand, session, request.seatCount()))
                .toList();

        double baseTotal = lineItems.stream()
                .mapToDouble(QuoteLineItemResponse::subtotal)
                .sum();

        boolean weekendPass = isWeekendPass(sessions);
        double weekendDiscount = weekendPass ? baseTotal * WEEKEND_DISCOUNT_RATE : 0.0;

        double afterWeekendDiscount = baseTotal - weekendDiscount;

        LoyaltyTier loyaltyTier = spectator == null ? LoyaltyTier.NONE : spectator.getLoyaltyTier();
        double loyaltyDiscount = afterWeekendDiscount * getLoyaltyDiscountRate(loyaltyTier);

        double afterLoyaltyDiscount = afterWeekendDiscount - loyaltyDiscount;

        boolean youth = spectator != null && isYouth(spectator.getBirthDate());
        double youthDiscount = youth ? afterLoyaltyDiscount * YOUTH_DISCOUNT_RATE : 0.0;

        double total = afterLoyaltyDiscount - youthDiscount;

        return new QuoteResponse(
                lineItems,
                round(baseTotal),
                round(weekendDiscount),
                weekendPass,
                loyaltyTier,
                round(loyaltyDiscount),
                round(youthDiscount),
                round(total)
        );
    }

    private Spectator getSpectator(Long spectatorId) {
        if (spectatorId == null) {
            return null;
        }

        return spectatorRepository.findById(spectatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Spectateur introuvable"));
    }

    private QuoteLineItemResponse toLineItem(Grandstand grandstand, RaceSession session, int seatCount) {
        double unitPrice = grandstand.getBasePrice() * session.getPriceMultiplier();
        double subtotal = unitPrice * seatCount;

        return new QuoteLineItemResponse(
                session.getId(),
                session.getDay() + " — " + session.getType(),
                round(unitPrice),
                round(subtotal)
        );
    }

    private boolean isWeekendPass(List<RaceSession> sessions) {
        Set<Day> days = sessions.stream()
                .map(RaceSession::getDay)
                .collect(Collectors.toSet());

        return days.contains(Day.FRIDAY)
                && days.contains(Day.SATURDAY)
                && days.contains(Day.SUNDAY);
    }

    private double getLoyaltyDiscountRate(LoyaltyTier loyaltyTier) {
        return switch (loyaltyTier) {
            case SILVER -> SILVER_DISCOUNT_RATE;
            case GOLD -> GOLD_DISCOUNT_RATE;
            case NONE -> 0.0;
        };
    }

    private boolean isYouth(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears() < YOUTH_AGE_LIMIT;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
