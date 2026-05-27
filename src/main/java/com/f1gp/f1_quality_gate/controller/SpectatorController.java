package com.f1gp.f1_quality_gate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.f1gp.f1_quality_gate.dto.spectator.SpectatorRequest;
import com.f1gp.f1_quality_gate.dto.spectator.SpectatorResponse;
import com.f1gp.f1_quality_gate.service.SpectatorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/spectators")
public class SpectatorController {

    private final SpectatorService spectatorService;

    public SpectatorController(SpectatorService spectatorService) {
        this.spectatorService = spectatorService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public SpectatorResponse createSpectator(@Valid @RequestBody SpectatorRequest request) {
        return spectatorService.createSpectator(request);
    }

    @GetMapping("")
    public List<SpectatorResponse> getSpectators() {
        return spectatorService.getSpectators();
    }
}
