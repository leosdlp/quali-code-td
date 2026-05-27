package com.f1gp.f1_quality_gate.controller;

import com.f1gp.f1_quality_gate.dto.grandstand.GrandstandRequest;
import com.f1gp.f1_quality_gate.dto.grandstand.GrandstandResponse;
import com.f1gp.f1_quality_gate.model.enums.Category;
import com.f1gp.f1_quality_gate.service.GrandstandService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrandstandController {

    private final GrandstandService grandstandService;

    public GrandstandController(GrandstandService grandstandService) {
        this.grandstandService = grandstandService;
    }

    @PostMapping("/grandstands")
    @ResponseStatus(HttpStatus.CREATED)
    public GrandstandResponse createGrandstand(@Valid @RequestBody GrandstandRequest request) {
        return grandstandService.createGrandstand(request);
    }

    @GetMapping("/grandstands")
    public List<GrandstandResponse> getGrandstands(@RequestParam(required = false) Category category) {
        return grandstandService.getGrandstands(category);
    }
}
