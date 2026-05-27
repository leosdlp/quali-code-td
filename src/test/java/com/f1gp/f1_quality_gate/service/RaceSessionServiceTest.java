package com.f1gp.f1_quality_gate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.f1gp.f1_quality_gate.dto.session.RaceSessionRequest;
import com.f1gp.f1_quality_gate.dto.session.RaceSessionResponse;
import com.f1gp.f1_quality_gate.model.entity.RaceSession;
import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import com.f1gp.f1_quality_gate.repository.RaceSessionRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RaceSessionServiceTest {

    @Mock
    private RaceSessionRepository raceSessionRepository;

    @InjectMocks
    private RaceSessionService raceSessionService;

    @Test
    void createSession_shouldUseCustomMultiplierWhenProvided() {
        LocalDateTime date = LocalDateTime.of(2026, 7, 19, 14, 0);
        RaceSessionRequest request = new RaceSessionRequest(Day.SUNDAY, SessionType.RACE, date, 2.0);

        when(raceSessionRepository.save(any(RaceSession.class)))
                .thenReturn(new RaceSession(1L, Day.SUNDAY, SessionType.RACE, date, 2.0));

        RaceSessionResponse response = raceSessionService.createSession(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.priceMultiplier()).isEqualTo(2.0);
        verify(raceSessionRepository).save(any(RaceSession.class));
    }

    @Test
    void createSession_shouldUseDefaultMultiplierWhenMultiplierIsNull() {
        LocalDateTime date = LocalDateTime.of(2026, 7, 19, 14, 0);
        RaceSessionRequest request = new RaceSessionRequest(Day.SUNDAY, SessionType.RACE, date, null);

        when(raceSessionRepository.save(any(RaceSession.class)))
                .thenReturn(new RaceSession(1L, Day.SUNDAY, SessionType.RACE, date, 1.8));

        RaceSessionResponse response = raceSessionService.createSession(request);

        assertThat(response.priceMultiplier()).isEqualTo(1.8);
        verify(raceSessionRepository).save(any(RaceSession.class));
    }

    @Test
    void getSessions_shouldReturnAllSessions() {
        LocalDateTime date = LocalDateTime.of(2026, 7, 18, 16, 0);

        when(raceSessionRepository.findAll()).thenReturn(List.of(
                new RaceSession(1L, Day.SATURDAY, SessionType.QUALIFYING, date, 1.0)
        ));

        List<RaceSessionResponse> responses = raceSessionService.getSessions();

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().type()).isEqualTo(SessionType.QUALIFYING);
        verify(raceSessionRepository).findAll();
    }
}
