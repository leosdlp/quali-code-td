package com.f1gp.f1_quality_gate.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.f1gp.f1_quality_gate.model.entity.Spectator;
import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpectatorRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private GrandstandRepository grandstandRepository;

    @Autowired
    private RaceSessionRepository raceSessionRepository;

    @Autowired
    private SpectatorRepository spectatorRepository;

    @BeforeEach
    void cleanDatabase() {
        reservationRepository.deleteAll();
        spectatorRepository.deleteAll();
        grandstandRepository.deleteAll();
        raceSessionRepository.deleteAll();
    }

    @Test
    void existsByEmail_shouldReturnTrueWhenEmailExists() {
        Spectator spectator = new Spectator(
                null,
                "Alice Martin",
                "alice@example.com",
                LocalDate.of(1990, 4, 12),
                LoyaltyTier.SILVER,
                LocalDateTime.now()
        );

        spectatorRepository.save(spectator);

        assertThat(spectatorRepository.existsByEmail("alice@example.com")).isTrue();
    }

    @Test
    void findByEmail_shouldReturnSpectatorWhenEmailExists() {
        Spectator spectator = new Spectator(
                null,
                "Bob Dupont",
                "bob@example.com",
                LocalDate.of(1995, 6, 20),
                LoyaltyTier.GOLD,
                LocalDateTime.now()
        );

        spectatorRepository.save(spectator);

        assertThat(spectatorRepository.findByEmail("bob@example.com"))
                .isPresent()
                .get()
                .extracting(Spectator::getName)
                .isEqualTo("Bob Dupont");
    }
}
