package com.f1gp.f1_quality_gate.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.f1gp.f1_quality_gate.dto.spectator.SpectatorRequest;
import com.f1gp.f1_quality_gate.dto.spectator.SpectatorResponse;
import com.f1gp.f1_quality_gate.exception.BusinessConflictException;
import com.f1gp.f1_quality_gate.model.entity.Spectator;
import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import com.f1gp.f1_quality_gate.repository.SpectatorRepository;

@Service
public class SpectatorService {

    private final SpectatorRepository spectatorRepository;

    public SpectatorService(SpectatorRepository spectatorRepository) {
        this.spectatorRepository = spectatorRepository;
    }

    public SpectatorResponse createSpectator(SpectatorRequest request) {
        if (spectatorRepository.existsByEmail(request.email())) {
            throw new BusinessConflictException("Email déjà utilisé");
        }

        Spectator spectator = new Spectator(
                null,
                request.name(),
                request.email(),
                request.birthDate(),
                request.loyaltyTier() == null ? LoyaltyTier.NONE : request.loyaltyTier(),
                LocalDateTime.now()
        );

        return toResponse(spectatorRepository.save(spectator));
    }

    public List<SpectatorResponse> getSpectators() {
        return spectatorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private SpectatorResponse toResponse(Spectator spectator) {
        return new SpectatorResponse(
                spectator.getId(),
                spectator.getName(),
                spectator.getEmail(),
                spectator.getBirthDate(),
                spectator.getLoyaltyTier(),
                spectator.getCreatedAt()
        );
    }
}
