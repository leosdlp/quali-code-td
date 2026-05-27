package com.f1gp.f1_quality_gate.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteLineItemResponse;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteRequest;
import com.f1gp.f1_quality_gate.dto.reservation.QuoteResponse;
import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import com.f1gp.f1_quality_gate.service.PricingService;
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
}
