package com.f1gp.f1_quality_gate.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.f1gp.f1_quality_gate.dto.spectator.SpectatorRequest;
import com.f1gp.f1_quality_gate.dto.spectator.SpectatorResponse;
import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import com.f1gp.f1_quality_gate.service.SpectatorService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SpectatorController.class)
class SpectatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SpectatorService spectatorService;

    @Test
    void createSpectator_shouldReturnCreatedSpectator() throws Exception {
        SpectatorRequest request = new SpectatorRequest(
                "Alice Martin",
                "alice@example.com",
                LocalDate.of(1990, 4, 12),
                LoyaltyTier.SILVER
        );

        SpectatorResponse response = new SpectatorResponse(
                1L,
                "Alice Martin",
                "alice@example.com",
                LocalDate.of(1990, 4, 12),
                LoyaltyTier.SILVER,
                LocalDateTime.of(2026, 5, 27, 12, 0)
        );

        when(spectatorService.createSpectator(any(SpectatorRequest.class))).thenReturn(response);

        mockMvc.perform(post("/spectators")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.loyaltyTier").value("SILVER"));
    }

    @Test
    void getSpectators_shouldReturnSpectators() throws Exception {
        when(spectatorService.getSpectators()).thenReturn(List.of(
                new SpectatorResponse(
                        1L,
                        "Alice Martin",
                        "alice@example.com",
                        LocalDate.of(1990, 4, 12),
                        LoyaltyTier.SILVER,
                        LocalDateTime.of(2026, 5, 27, 12, 0)
                )
        ));

        mockMvc.perform(get("/spectators"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"));
    }

    @Test
    void createSpectator_shouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        SpectatorRequest request = new SpectatorRequest(
                "",
                "invalid-email",
                LocalDate.now().plusDays(1),
                LoyaltyTier.SILVER
        );

        mockMvc.perform(post("/spectators")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
