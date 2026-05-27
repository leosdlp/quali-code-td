package com.f1gp.f1_quality_gate.model.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.Category;
import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.LoyaltyTier;
import com.f1gp.f1_quality_gate.model.enums.ReservationStatus;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    void shouldCreateReservationWithNoArgsConstructor() {
        Reservation reservation = new Reservation();

        assertThat(reservation).isNotNull();
        assertThat(reservation.getSessions()).isEmpty();
    }

    @Test
    void shouldCreateReservationWithAllArgsConstructor() {
        Spectator spectator = createSpectator();
        Grandstand grandstand = createGrandstand();
        RaceSession raceSession = createRaceSession();
        LocalDateTime bookedAt = LocalDateTime.of(2026, 5, 27, 12, 0);

        Reservation reservation = new Reservation(
                1L,
                spectator,
                grandstand,
                List.of(raceSession),
                2,
                360.0,
                ReservationStatus.CONFIRMED,
                bookedAt,
                null,
                0.0
        );

        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getSpectator()).isEqualTo(spectator);
        assertThat(reservation.getGrandstand()).isEqualTo(grandstand);
        assertThat(reservation.getSessions()).containsExactly(raceSession);
        assertThat(reservation.getSeatCount()).isEqualTo(2);
        assertThat(reservation.getTotalPrice()).isEqualTo(360.0);
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(reservation.getBookedAt()).isEqualTo(bookedAt);
        assertThat(reservation.getCancelledAt()).isNull();
        assertThat(reservation.getRefundedAmount()).isEqualTo(0.0);
    }

    @Test
    void shouldUpdateReservationWithSetters() {
        Spectator spectator = createSpectator();
        Grandstand grandstand = createGrandstand();
        RaceSession raceSession = createRaceSession();
        LocalDateTime bookedAt = LocalDateTime.of(2026, 5, 27, 12, 0);
        LocalDateTime cancelledAt = LocalDateTime.of(2026, 5, 28, 10, 0);

        Reservation reservation = new Reservation();

        reservation.setId(2L);
        reservation.setSpectator(spectator);
        reservation.setGrandstand(grandstand);
        reservation.setSessions(List.of(raceSession));
        reservation.setSeatCount(3);
        reservation.setTotalPrice(540.0);
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setBookedAt(bookedAt);
        reservation.setCancelledAt(cancelledAt);
        reservation.setRefundedAmount(540.0);

        assertThat(reservation.getId()).isEqualTo(2L);
        assertThat(reservation.getSpectator()).isEqualTo(spectator);
        assertThat(reservation.getGrandstand()).isEqualTo(grandstand);
        assertThat(reservation.getSessions()).containsExactly(raceSession);
        assertThat(reservation.getSeatCount()).isEqualTo(3);
        assertThat(reservation.getTotalPrice()).isEqualTo(540.0);
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(reservation.getBookedAt()).isEqualTo(bookedAt);
        assertThat(reservation.getCancelledAt()).isEqualTo(cancelledAt);
        assertThat(reservation.getRefundedAmount()).isEqualTo(540.0);
    }

    private Spectator createSpectator() {
        return new Spectator(
                1L,
                "Alice Martin",
                "alice@example.com",
                LocalDate.of(1990, 4, 12),
                LoyaltyTier.SILVER,
                LocalDateTime.of(2026, 5, 27, 12, 0)
        );
    }

    private Grandstand createGrandstand() {
        return new Grandstand(
                1L,
                "Tribune Sainte-Beaume",
                "Virage 3",
                Category.GOLD,
                200,
                180.0,
                true
        );
    }

    private RaceSession createRaceSession() {
        return new RaceSession(
                1L,
                Day.SUNDAY,
                SessionType.RACE,
                LocalDateTime.of(2026, 7, 19, 14, 0),
                1.8
        );
    }
}
