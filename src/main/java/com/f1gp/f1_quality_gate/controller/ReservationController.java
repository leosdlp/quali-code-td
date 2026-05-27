package com.f1gp.f1_quality_gate.controller;

import com.f1gp.f1_quality_gate.dto.reservation.QuoteRequest;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteResponse;
import com.f1gp.f1_quality_gate.service.PricingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private final PricingService pricingService;

    public ReservationController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping("/reservations/quote")
    public QuoteResponse quote(@Valid @RequestBody QuoteRequest request) {
        return pricingService.quote(request);
    }
}
