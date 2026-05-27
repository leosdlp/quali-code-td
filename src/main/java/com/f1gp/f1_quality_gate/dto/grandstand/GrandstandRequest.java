package com.f1gp.f1_quality_gate.dto.grandstand;

import com.f1gp.f1_quality_gate.model.enums.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GrandstandRequest(
        @NotBlank String name,
        @NotBlank String location,
        @NotNull Category category,
        @Min(1) int capacity,
        @DecimalMin("0.01") double basePrice,
        boolean covered
) {
}
