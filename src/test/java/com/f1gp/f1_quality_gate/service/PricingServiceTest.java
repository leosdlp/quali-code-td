package com.f1gp.f1_quality_gate.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.f1gp.f1_quality_gate.dto.reservation.QuoteRequest;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteResponse;
import com.f1gp.f1_quality_gate.exception.ResourceNotFoundException;
import com.f1gp.f1_quality_gate.model.entity.Grandstand;
import com.f1gp.f1_quality_gate.model.entity.RaceSession;
import com.f1gp.f1_quality_gate.model.entity.Spectator;
import com.f1gp.f1_quality_gate.model.enums.Category;
import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import com.f1gp.f1_quality_gate.repository.GrandstandRepository;
import com.f1gp.f1_quality_gate.repository.RaceSessionRepository;
import com.f1gp.f1_quality_gate.repository.SpectatorRepository;

@ExtendWith(MockitoExtension.class)
class PricingServiceTest {

    @Mock
    private GrandstandRepository grandstandRepository;

    @Mock
    private RaceSessionRepository raceSessionRepository;

    @Mock
    private SpectatorRepository spectatorRepository;

    @InjectMocks
    private PricingService pricingService;

    @Test
    void quote_shouldCalculateBasePriceWithoutSpectator() {
        Grandstand grandstand = createGrandstand();
        RaceSession session = createSession(1L, Day.SUNDAY, SessionType.RACE, 1.8);

        when(grandstandRepository.findById(1L)).thenReturn(Optional.of(grandstand));
        when(raceSessionRepository.findAllById(List.of(1L))).thenReturn(List.of(session));

        QuoteResponse response = pricingService.quote(new QuoteRequest(1L, List.of(1L), 2, null));

        assertThat(response.baseTotal()).isEqualTo(648.0);
        assertThat(response.total()).isEqualTo(648.0);
        assertThat(response.loyaltyTier()).isEqualTo(LoyaltyTier.NONE);
    }

    @Test
    void quote_shouldApplyWeekendDiscount() {
        Grandstand grandstand = createGrandstand();
        List<RaceSession> sessions = List.of(
                createSession(1L, Day.FRIDAY, SessionType.PRACTICE, 0.5),
                createSession(2L, Day.SATURDAY, SessionType.QUALIFYING, 1.0),
                createSession(3L, Day.SUNDAY, SessionType.RACE, 1.8)
        );

        when(grandstandRepository.findById(1L)).thenReturn(Optional.of(grandstand));
        when(raceSessionRepository.findAllById(List.of(1L, 2L, 3L))).thenReturn(sessions);

        QuoteResponse response = pricingService.quote(new QuoteRequest(1L, List.of(1L, 2L, 3L), 2, null));

        assertThat(response.baseTotal()).isEqualTo(1188.0);
        assertThat(response.isWeekendPass()).isTrue();
        assertThat(response.weekendDiscount()).isEqualTo(237.6);
        assertThat(response.total()).isEqualTo(950.4);
    }

    @Test
    void quote_shouldApplyGoldDiscountAfterWeekendDiscount() {
        Grandstand grandstand = createGrandstand();
        Spectator spectator = createSpectator(LocalDate.of(1990, 4, 12), LoyaltyTier.GOLD);

        List<RaceSession> sessions = List.of(
                createSession(1L, Day.FRIDAY, SessionType.PRACTICE, 0.5),
                createSession(2L, Day.SATURDAY, SessionType.QUALIFYING, 1.0),
                createSession(3L, Day.SUNDAY, SessionType.RACE, 1.8)
        );

        when(grandstandRepository.findById(1L)).thenReturn(Optional.of(grandstand));
        when(raceSessionRepository.findAllById(List.of(1L, 2L, 3L))).thenReturn(sessions);
        when(spectatorRepository.findById(10L)).thenReturn(Optional.of(spectator));

        QuoteResponse response = pricingService.quote(new QuoteRequest(1L, List.of(1L, 2L, 3L), 2, 10L));

        assertThat(response.baseTotal()).isEqualTo(1188.0);
        assertThat(response.weekendDiscount()).isEqualTo(237.6);
        assertThat(response.loyaltyDiscount()).isEqualTo(95.04);
        assertThat(response.total()).isEqualTo(855.36);
    }

    @Test
    void quote_shouldApplyYouthDiscountAfterOtherDiscounts() {
        Grandstand grandstand = createGrandstand();
        Spectator spectator = createSpectator(LocalDate.now().minusYears(12), LoyaltyTier.SILVER);
        RaceSession session = createSession(1L, Day.SUNDAY, SessionType.RACE, 1.8);

        when(grandstandRepository.findById(1L)).thenReturn(Optional.of(grandstand));
        when(raceSessionRepository.findAllById(List.of(1L))).thenReturn(List.of(session));
        when(spectatorRepository.findById(10L)).thenReturn(Optional.of(spectator));

        QuoteResponse response = pricingService.quote(new QuoteRequest(1L, List.of(1L), 2, 10L));

        assertThat(response.baseTotal()).isEqualTo(648.0);
        assertThat(response.loyaltyDiscount()).isEqualTo(32.4);
        assertThat(response.youthDiscount()).isEqualTo(307.8);
        assertThat(response.total()).isEqualTo(307.8);
    }

    @Test
    void quote_shouldThrowExceptionWhenGrandstandDoesNotExist() {
        QuoteRequest request = new QuoteRequest(1L, List.of(1L), 2, null);

        when(grandstandRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pricingService.quote(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tribune introuvable");
    }

    @Test
    void quote_shouldThrowExceptionWhenSessionDoesNotExist() {
        QuoteRequest request = new QuoteRequest(1L, List.of(1L, 2L), 2, null);

        when(grandstandRepository.findById(1L)).thenReturn(Optional.of(createGrandstand()));
        when(raceSessionRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(createSession(1L, Day.SUNDAY, SessionType.RACE, 1.8)));

        assertThatThrownBy(() -> pricingService.quote(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Session introuvable");
    }

    private Grandstand createGrandstand() {
        return new Grandstand(1L, "Tribune Sainte-Beaume", "Virage 3", Category.GOLD, 200, 180.0, true);
    }

    private RaceSession createSession(Long id, Day day, SessionType type, double multiplier) {
        return new RaceSession(id, day, type, LocalDateTime.of(2026, 7, 19, 14, 0), multiplier);
    }

    private Spectator createSpectator(LocalDate birthDate, LoyaltyTier loyaltyTier) {
        return new Spectator(10L, "Alice Martin", "alice@example.com", birthDate, loyaltyTier, LocalDateTime.now());
    }
}
