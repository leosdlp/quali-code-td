package com.f1gp.f1_quality_gate.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.f1gp.f1_quality_gate.dto.reservation.QuoteResponse;
import com.f1gp.f1_quality_gate.dto.reservation.ReservationRequest;
import com.f1gp.f1_quality_gate.dto.reservation.ReservationResponse;
import com.f1gp.f1_quality_gate.exception.ResourceNotFoundException;
import com.f1gp.f1_quality_gate.model.entity.Grandstand;
import com.f1gp.f1_quality_gate.model.entity.RaceSession;
import com.f1gp.f1_quality_gate.model.entity.Reservation;
import com.f1gp.f1_quality_gate.model.entity.Spectator;
import com.f1gp.f1_quality_gate.model.enums.Category;
import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import com.f1gp.f1_quality_gate.model.enums.ReservationStatus;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import com.f1gp.f1_quality_gate.repository.GrandstandRepository;
import com.f1gp.f1_quality_gate.repository.RaceSessionRepository;
import com.f1gp.f1_quality_gate.repository.ReservationRepository;
import com.f1gp.f1_quality_gate.repository.SpectatorRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SpectatorRepository spectatorRepository;

    @Mock
    private GrandstandRepository grandstandRepository;

    @Mock
    private RaceSessionRepository raceSessionRepository;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private RefundService refundService;

    @Test
    void createReservation_shouldCreateConfirmedReservation() {
        Spectator spectator = createSpectator();
        Grandstand grandstand = createGrandstand(200);
        RaceSession session = createSession();
        ReservationRequest request = new ReservationRequest(1L, 2L, List.of(3L), 2);

        when(spectatorRepository.findById(1L)).thenReturn(Optional.of(spectator));
        when(grandstandRepository.findById(2L)).thenReturn(Optional.of(grandstand));
        when(raceSessionRepository.findAllById(List.of(3L))).thenReturn(List.of(session));
        when(reservationRepository.findByGrandstandAndSessionsContainingAndStatus(grandstand, session, ReservationStatus.CONFIRMED))
                .thenReturn(List.of());
        when(pricingService.quote(any())).thenReturn(new QuoteResponse(List.of(), 648.0, 0.0, false, LoyaltyTier.NONE, 0.0, 0.0, 648.0));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation reservation = invocation.getArgument(0);
            reservation.setId(10L);
            return reservation;
        });

        ReservationResponse response = reservationService.createReservation(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.status()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(response.totalPrice()).isEqualTo(648.0);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void createReservation_shouldThrowExceptionWhenSpectatorDoesNotExist() {
        ReservationRequest request = new ReservationRequest(1L, 2L, List.of(3L), 2);
        when(spectatorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Spectateur introuvable");
    }

    @Test
    void createReservation_shouldThrowExceptionWhenNotEnoughSeats() {
        Spectator spectator = createSpectator();
        Grandstand grandstand = createGrandstand(2);
        RaceSession session = createSession();
        Reservation existingReservation = createReservation(spectator, grandstand, session, 2);
        ReservationRequest request = new ReservationRequest(1L, 2L, List.of(3L), 1);

        when(spectatorRepository.findById(1L)).thenReturn(Optional.of(spectator));
        when(grandstandRepository.findById(2L)).thenReturn(Optional.of(grandstand));
        when(raceSessionRepository.findAllById(List.of(3L))).thenReturn(List.of(session));
        when(reservationRepository.findByGrandstandAndSessionsContainingAndStatus(grandstand, session, ReservationStatus.CONFIRMED))
                .thenReturn(List.of(existingReservation));

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Pas assez de places sur la session SUNDAY — RACE");
    }

    @Test
    void getReservations_shouldReturnAllReservationsWhenSpectatorIdIsNull() {
        Spectator spectator = createSpectator();
        Grandstand grandstand = createGrandstand(200);
        RaceSession session = createSession();

        when(reservationRepository.findAll()).thenReturn(List.of(createReservation(spectator, grandstand, session, 2)));

        List<ReservationResponse> responses = reservationService.getReservations(null);

        assertThat(responses).hasSize(1);
        verify(reservationRepository).findAll();
    }

    @Test
    void getReservations_shouldFilterBySpectatorId() {
        Spectator spectator = createSpectator();
        Grandstand grandstand = createGrandstand(200);
        RaceSession session = createSession();

        when(reservationRepository.findBySpectatorId(1L)).thenReturn(List.of(createReservation(spectator, grandstand, session, 2)));

        List<ReservationResponse> responses = reservationService.getReservations(1L);

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().spectatorId()).isEqualTo(1L);
        verify(reservationRepository).findBySpectatorId(1L);
    }

    @Test
    void cancelReservation_shouldCancelReservationWithFullRefund() {
        Spectator spectator = createSpectator();
        Grandstand grandstand = createGrandstand(200);
        RaceSession session = createSession();
        Reservation reservation = createReservation(spectator, grandstand, session, 2);

        when(reservationRepository.findById(10L)).thenReturn(Optional.of(reservation));
        when(refundService.calculateDaysUntilFirstSession(any(), any())).thenReturn(15L);
        when(refundService.calculateRefundRate(any(), any())).thenReturn(1.0);
        when(refundService.calculateRefundAmount(648.0, 1.0)).thenReturn(648.0);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = reservationService.cancelReservation(10L);

        assertThat(response.reservation().status()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(response.refund()).isEqualTo(648.0);
        assertThat(response.rate()).isEqualTo(1.0);
        assertThat(response.daysUntilFirstSession()).isEqualTo(15L);
    }

    @Test
    void cancelReservation_shouldThrowExceptionWhenReservationDoesNotExist() {
        when(reservationRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.cancelReservation(10L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Réservation introuvable");
    }

    @Test
    void cancelReservation_shouldThrowExceptionWhenAlreadyCancelled() {
        Spectator spectator = createSpectator();
        Grandstand grandstand = createGrandstand(200);
        RaceSession session = createSession();
        Reservation reservation = createReservation(spectator, grandstand, session, 2);
        reservation.setStatus(ReservationStatus.CANCELLED);

        when(reservationRepository.findById(10L)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.cancelReservation(10L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Réservation déjà annulée");
    }

    private Spectator createSpectator() {
        return new Spectator(1L, "Alice Martin", "alice@example.com", LocalDate.of(1990, 4, 12), LoyaltyTier.NONE, LocalDateTime.now());
    }

    private Grandstand createGrandstand(int capacity) {
        return new Grandstand(2L, "Tribune Sainte-Beaume", "Virage 3", Category.GOLD, capacity, 180.0, true);
    }

    private RaceSession createSession() {
        return new RaceSession(3L, Day.SUNDAY, SessionType.RACE, LocalDateTime.of(2026, 7, 19, 14, 0), 1.8);
    }

    private Reservation createReservation(Spectator spectator, Grandstand grandstand, RaceSession session, int seatCount) {
        return new Reservation(10L, spectator, grandstand, List.of(session), seatCount, 648.0, ReservationStatus.CONFIRMED, LocalDateTime.now(), null, 0.0);
    }
}
