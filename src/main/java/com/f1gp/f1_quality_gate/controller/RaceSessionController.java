package com.f1gp.f1_quality_gate.controller;

import com.f1gp.f1_quality_gate.dto.session.RaceSessionRequest;
import com.f1gp.f1_quality_gate.dto.session.RaceSessionResponse;
import com.f1gp.f1_quality_gate.service.RaceSessionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RaceSessionController {

    private final RaceSessionService raceSessionService;

    public RaceSessionController(RaceSessionService raceSessionService) {
        this.raceSessionService = raceSessionService;
    }

    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public RaceSessionResponse createSession(@Valid @RequestBody RaceSessionRequest request) {
        return raceSessionService.createSession(request);
    }

    @GetMapping("/sessions")
    public List<RaceSessionResponse> getSessions() {
        return raceSessionService.getSessions();
    }
}
