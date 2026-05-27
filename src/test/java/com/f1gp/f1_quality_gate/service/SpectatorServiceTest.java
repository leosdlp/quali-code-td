package com.f1gp.f1_quality_gate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.f1gp.f1_quality_gate.dto.spectator.SpectatorRequest;
import com.f1gp.f1_quality_gate.dto.spectator.SpectatorResponse;
import com.f1gp.f1_quality_gate.model.entity.Spectator;
import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import com.f1gp.f1_quality_gate.repository.SpectatorRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpectatorServiceTest {

    @Mock
    private SpectatorRepository spectatorRepository;

    @InjectMocks
    private SpectatorService spectatorService;

    @Test
    void createSpectator_shouldSaveSpectator() {
        SpectatorRequest request = new SpectatorRequest(
                "Alice Martin",
                "alice@example.com",
                LocalDate.of(1990, 4, 12),
                LoyaltyTier.SILVER
        );

        Spectator savedSpectator = new Spectator(
                1L,
                "Alice Martin",
                "alice@example.com",
                LocalDate.of(1990, 4, 12),
                LoyaltyTier.SILVER,
                LocalDateTime.now()
        );

        when(spectatorRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(spectatorRepository.save(any(Spectator.class))).thenReturn(savedSpectator);

        SpectatorResponse response = spectatorService.createSpectator(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("alice@example.com");
        assertThat(response.loyaltyTier()).isEqualTo(LoyaltyTier.SILVER);
        verify(spectatorRepository).save(any(Spectator.class));
    }

    @Test
    void createSpectator_shouldUseNoneLoyaltyTierWhenNull() {
        SpectatorRequest request = new SpectatorRequest(
                "Bob Dupont",
                "bob@example.com",
                LocalDate.of(1995, 6, 20),
                null
        );

        Spectator savedSpectator = new Spectator(
                2L,
                "Bob Dupont",
                "bob@example.com",
                LocalDate.of(1995, 6, 20),
                LoyaltyTier.NONE,
                LocalDateTime.now()
        );

        when(spectatorRepository.existsByEmail("bob@example.com")).thenReturn(false);
        when(spectatorRepository.save(any(Spectator.class))).thenReturn(savedSpectator);

        SpectatorResponse response = spectatorService.createSpectator(request);

        assertThat(response.loyaltyTier()).isEqualTo(LoyaltyTier.NONE);
    }

    @Test
    void createSpectator_shouldThrowExceptionWhenEmailAlreadyExists() {
        SpectatorRequest request = new SpectatorRequest(
                "Alice Martin",
                "alice@example.com",
                LocalDate.of(1990, 4, 12),
                LoyaltyTier.SILVER
        );

        when(spectatorRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> spectatorService.createSpectator(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email déjà utilisé");
    }

    @Test
    void getSpectators_shouldReturnAllSpectators() {
        when(spectatorRepository.findAll()).thenReturn(List.of(
                new Spectator(1L, "Alice Martin", "alice@example.com", LocalDate.of(1990, 4, 12), LoyaltyTier.SILVER, LocalDateTime.now())
        ));

        List<SpectatorResponse> responses = spectatorService.getSpectators();

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().email()).isEqualTo("alice@example.com");
        verify(spectatorRepository).findAll();
    }
}
