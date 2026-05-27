package com.f1gp.f1_quality_gate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.f1gp.f1_quality_gate.dto.reservation.CancelResultResponse;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteRequest;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteResponse;
import com.f1gp.f1_quality_gate.dto.reservation.ReservationRequest;
import com.f1gp.f1_quality_gate.dto.reservation.ReservationResponse;
import com.f1gp.f1_quality_gate.service.PricingService;
import com.f1gp.f1_quality_gate.service.ReservationService;

import jakarta.validation.Valid;

@RestController
public class ReservationController {

    private final PricingService pricingService;
    private final ReservationService reservationService;

    public ReservationController(PricingService pricingService, ReservationService reservationService) {
        this.pricingService = pricingService;
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations/quote")
    public QuoteResponse quote(@Valid @RequestBody QuoteRequest request) {
        return pricingService.quote(request);
    }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(@Valid @RequestBody ReservationRequest request) {
        return reservationService.createReservation(request);
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> getReservations(@RequestParam(required = false) Long spectatorId) {
        return reservationService.getReservations(spectatorId);
    }

    @PostMapping("/reservations/{id}/cancel")
    public CancelResultResponse cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }
}
