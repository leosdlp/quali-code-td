package com.f1gp.f1_quality_gate.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.f1gp.f1_quality_gate.dto.grandstand.GrandstandRequest;
import com.f1gp.f1_quality_gate.dto.grandstand.GrandstandResponse;
import com.f1gp.f1_quality_gate.model.enums.Category;
import com.f1gp.f1_quality_gate.service.GrandstandService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GrandstandController.class)
class GrandstandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GrandstandService grandstandService;

    @Test
    void createGrandstand_shouldReturnCreatedGrandstand() throws Exception {
        GrandstandRequest request = new GrandstandRequest(
                "Tribune Sainte-Beaume",
                "Virage 3",
                Category.GOLD,
                200,
                180.0,
                true
        );

        GrandstandResponse response = new GrandstandResponse(
                1L,
                "Tribune Sainte-Beaume",
                "Virage 3",
                Category.GOLD,
                200,
                180.0,
                true
        );

        when(grandstandService.createGrandstand(any(GrandstandRequest.class))).thenReturn(response);

        mockMvc.perform(post("/grandstands")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tribune Sainte-Beaume"))
                .andExpect(jsonPath("$.category").value("GOLD"));
    }

    @Test
    void getGrandstands_shouldReturnGrandstands() throws Exception {
        when(grandstandService.getGrandstands(null)).thenReturn(List.of(
                new GrandstandResponse(1L, "Bronze Stand", "Turn 1", Category.BRONZE, 100, 80.0, false)
        ));

        mockMvc.perform(get("/grandstands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].category").value("BRONZE"));
    }

    @Test
    void getGrandstands_shouldFilterGrandstandsByCategory() throws Exception {
        when(grandstandService.getGrandstands(Category.GOLD)).thenReturn(List.of(
                new GrandstandResponse(2L, "Gold Stand", "Turn 3", Category.GOLD, 200, 180.0, true)
        ));

        mockMvc.perform(get("/grandstands?category=GOLD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].category").value("GOLD"));
    }

    @Test
    void createGrandstand_shouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        GrandstandRequest request = new GrandstandRequest(
                "",
                "",
                null,
                0,
                0.0,
                false
        );

        mockMvc.perform(post("/grandstands")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
