package com.f1gp.f1_quality_gate.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteLineItemResponse;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteRequest;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteResponse;
import com.f1gp.f1_quality_gate.dto.reservation.ReservationRequest;
import com.f1gp.f1_quality_gate.dto.reservation.ReservationResponse;
import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import com.f1gp.f1_quality_gate.model.enums.ReservationStatus;
import com.f1gp.f1_quality_gate.service.PricingService;
import com.f1gp.f1_quality_gate.service.ReservationService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PricingService pricingService;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void quote_shouldReturnPriceDetails() throws Exception {
        QuoteRequest request = new QuoteRequest(1L, List.of(1L), 2, null);

        QuoteResponse response = new QuoteResponse(
                List.of(new QuoteLineItemResponse(1L, "SUNDAY — RACE", 324.0, 648.0)),
                648.0,
                0.0,
                false,
                LoyaltyTier.NONE,
                0.0,
                0.0,
                648.0
        );

        when(pricingService.quote(any(QuoteRequest.class))).thenReturn(response);

        mockMvc.perform(post("/reservations/quote")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseTotal").value(648.0))
                .andExpect(jsonPath("$.total").value(648.0))
                .andExpect(jsonPath("$.lineItems[0].sessionLabel").value("SUNDAY — RACE"));
    }

    @Test
    void quote_shouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        QuoteRequest request = new QuoteRequest(null, List.of(), 10, null);

        mockMvc.perform(post("/reservations/quote")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReservation_shouldReturnCreatedReservation() throws Exception {
        ReservationRequest request = new ReservationRequest(1L, 2L, List.of(3L), 2);
        ReservationResponse response = new ReservationResponse(
                10L,
                1L,
                2L,
                List.of(3L),
                2,
                648.0,
                ReservationStatus.CONFIRMED,
                LocalDateTime.of(2026, 5, 27, 12, 0),
                null,
                0.0
        );

        when(reservationService.createReservation(any(ReservationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/reservations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.totalPrice").value(648.0));
    }

    @Test
    void getReservations_shouldReturnReservations() throws Exception {
        when(reservationService.getReservations(null)).thenReturn(List.of(
                new ReservationResponse(10L, 1L, 2L, List.of(3L), 2, 648.0, ReservationStatus.CONFIRMED, LocalDateTime.now(), null, 0.0)
        ));

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
    }

    @Test
    void getReservations_shouldFilterBySpectatorId() throws Exception {
        when(reservationService.getReservations(1L)).thenReturn(List.of(
                new ReservationResponse(10L, 1L, 2L, List.of(3L), 2, 648.0, ReservationStatus.CONFIRMED, LocalDateTime.now(), null, 0.0)
        ));

        mockMvc.perform(get("/reservations?spectatorId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].spectatorId").value(1));
    }
}
