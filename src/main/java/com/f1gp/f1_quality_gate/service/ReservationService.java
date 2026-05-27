package com.f1gp.f1_quality_gate.service;

import com.f1gp.f1_quality_gate.dto.reservation.QuoteRequest;
import com.f1gp.f1_quality_gate.dto.reservation.ReservationRequest;
import com.f1gp.f1_quality_gate.dto.reservation.ReservationResponse;
import com.f1gp.f1_quality_gate.exception.ResourceNotFoundException;
import com.f1gp.f1_quality_gate.model.entity.Grandstand;
import com.f1gp.f1_quality_gate.model.entity.RaceSession;
import com.f1gp.f1_quality_gate.model.entity.Reservation;
import com.f1gp.f1_quality_gate.model.entity.Spectator;
import com.f1gp.f1_quality_gate.model.enums.ReservationStatus;
import com.f1gp.f1_quality_gate.repository.GrandstandRepository;
import com.f1gp.f1_quality_gate.repository.RaceSessionRepository;
import com.f1gp.f1_quality_gate.repository.ReservationRepository;
import com.f1gp.f1_quality_gate.repository.SpectatorRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SpectatorRepository spectatorRepository;
    private final GrandstandRepository grandstandRepository;
    private final RaceSessionRepository raceSessionRepository;
    private final PricingService pricingService;

    public ReservationService(
            ReservationRepository reservationRepository,
            SpectatorRepository spectatorRepository,
            GrandstandRepository grandstandRepository,
            RaceSessionRepository raceSessionRepository,
            PricingService pricingService
    ) {
        this.reservationRepository = reservationRepository;
        this.spectatorRepository = spectatorRepository;
        this.grandstandRepository = grandstandRepository;
        this.raceSessionRepository = raceSessionRepository;
        this.pricingService = pricingService;
    }

    public ReservationResponse createReservation(ReservationRequest request) {
        Spectator spectator = spectatorRepository.findById(request.spectatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Spectateur introuvable"));

        Grandstand grandstand = grandstandRepository.findById(request.grandstandId())
                .orElseThrow(() -> new ResourceNotFoundException("Tribune introuvable"));

        List<RaceSession> sessions = raceSessionRepository.findAllById(request.sessionIds());

        if (sessions.size() != request.sessionIds().size()) {
            throw new ResourceNotFoundException("Session introuvable");
        }

        validateAvailability(grandstand, sessions, request.seatCount());

        double totalPrice = pricingService.quote(new QuoteRequest(
                request.grandstandId(),
                request.sessionIds(),
                request.seatCount(),
                request.spectatorId()
        )).total();

        Reservation reservation = new Reservation(
                null,
                spectator,
                grandstand,
                sessions,
                request.seatCount(),
                totalPrice,
                ReservationStatus.CONFIRMED,
                LocalDateTime.now(),
                null,
                0.0
        );

        return toResponse(reservationRepository.save(reservation));
    }

    public List<ReservationResponse> getReservations(Long spectatorId) {
        List<Reservation> reservations = spectatorId == null
                ? reservationRepository.findAll()
                : reservationRepository.findBySpectatorId(spectatorId);

        return reservations.stream()
                .map(this::toResponse)
                .toList();
    }

    private void validateAvailability(Grandstand grandstand, List<RaceSession> sessions, int requestedSeats) {
        for (RaceSession session : sessions) {
            int alreadyBookedSeats = reservationRepository
                    .findByGrandstandAndSessionsContainingAndStatus(grandstand, session, ReservationStatus.CONFIRMED)
                    .stream()
                    .mapToInt(Reservation::getSeatCount)
                    .sum();

            int remainingSeats = grandstand.getCapacity() - alreadyBookedSeats;

            if (remainingSeats < requestedSeats) {
                throw new IllegalStateException("Pas assez de places sur la session " + session.getDay() + " — " + session.getType());
            }
        }
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getSpectator().getId(),
                reservation.getGrandstand().getId(),
                reservation.getSessions().stream().map(RaceSession::getId).toList(),
                reservation.getSeatCount(),
                reservation.getTotalPrice(),
                reservation.getStatus(),
                reservation.getBookedAt(),
                reservation.getCancelledAt(),
                reservation.getRefundedAmount()
        );
    }
}
