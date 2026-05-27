package com.f1gp.f1_quality_gate.model.entity;

import com.f1gp.f1_quality_gate.model.enums.ReservationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Spectator spectator;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Grandstand grandstand;

    @ManyToMany
    @JoinTable(
            name = "reservation_sessions",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "session_id")
    )
    private List<RaceSession> sessions = new ArrayList<>();

    @Min(1)
    @Max(6)
    private int seatCount;

    @DecimalMin(value = "0.0")
    private double totalPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @NotNull
    private LocalDateTime bookedAt;

    private LocalDateTime cancelledAt;

    @DecimalMin(value = "0.0")
    private double refundedAmount;
}
