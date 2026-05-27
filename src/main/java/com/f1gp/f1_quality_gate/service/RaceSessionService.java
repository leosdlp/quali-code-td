package com.f1gp.f1_quality_gate.service;

import com.f1gp.f1_quality_gate.dto.session.RaceSessionRequest;
import com.f1gp.f1_quality_gate.dto.session.RaceSessionResponse;
import com.f1gp.f1_quality_gate.model.entity.RaceSession;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import com.f1gp.f1_quality_gate.repository.RaceSessionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RaceSessionService {

    private final RaceSessionRepository raceSessionRepository;

    public RaceSessionService(RaceSessionRepository raceSessionRepository) {
        this.raceSessionRepository = raceSessionRepository;
    }

    public RaceSessionResponse createSession(RaceSessionRequest request) {
        RaceSession raceSession = new RaceSession(
                null,
                request.day(),
                request.type(),
                request.date(),
                resolvePriceMultiplier(request.type(), request.priceMultiplier())
        );

        return toResponse(raceSessionRepository.save(raceSession));
    }

    public List<RaceSessionResponse> getSessions() {
        return raceSessionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private double resolvePriceMultiplier(SessionType type, Double customMultiplier) {
        if (customMultiplier != null) {
            return customMultiplier;
        }

        return switch (type) {
            case PRACTICE -> 0.5;
            case QUALIFYING -> 1.0;
            case SPRINT -> 1.2;
            case RACE -> 1.8;
        };
    }

    private RaceSessionResponse toResponse(RaceSession raceSession) {
        return new RaceSessionResponse(
                raceSession.getId(),
                raceSession.getDay(),
                raceSession.getType(),
                raceSession.getDate(),
                raceSession.getPriceMultiplier()
        );
    }
}
