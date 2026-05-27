package com.f1gp.f1_quality_gate.model.entity;

import com.f1gp.f1_quality_gate.model.enums.Day;
import com.f1gp.f1_quality_gate.model.enums.SessionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RaceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Day day;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SessionType type;

    @NotNull
    private LocalDateTime date;

    @DecimalMin(value = "0.01")
    private double priceMultiplier;
}
