package com.f1gp.f1_quality_gate.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.f1gp.f1_quality_gate.dto.session.RaceSessionRequest;
import com.f1gp.f1_quality_gate.dto.session.RaceSessionResponse;
import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import com.f1gp.f1_quality_gate.service.RaceSessionService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RaceSessionController.class)
class RaceSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RaceSessionService raceSessionService;

    @Test
    void createSession_shouldReturnCreatedSession() throws Exception {
        LocalDateTime date = LocalDateTime.of(2026, 7, 19, 14, 0);
        RaceSessionRequest request = new RaceSessionRequest(Day.SUNDAY, SessionType.RACE, date, null);

        when(raceSessionService.createSession(any(RaceSessionRequest.class)))
                .thenReturn(new RaceSessionResponse(1L, Day.SUNDAY, SessionType.RACE, date, 1.8));

        mockMvc.perform(post("/sessions")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.day").value("SUNDAY"))
                .andExpect(jsonPath("$.type").value("RACE"))
                .andExpect(jsonPath("$.priceMultiplier").value(1.8));
    }

    @Test
    void getSessions_shouldReturnSessions() throws Exception {
        LocalDateTime date = LocalDateTime.of(2026, 7, 18, 16, 0);

        when(raceSessionService.getSessions()).thenReturn(List.of(
                new RaceSessionResponse(1L, Day.SATURDAY, SessionType.QUALIFYING, date, 1.0)
        ));

        mockMvc.perform(get("/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].day").value("SATURDAY"))
                .andExpect(jsonPath("$[0].type").value("QUALIFYING"));
    }

    @Test
    void createSession_shouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        RaceSessionRequest request = new RaceSessionRequest(null, null, null, 0.0);

        mockMvc.perform(post("/sessions")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
