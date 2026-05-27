package com.f1gp.f1_quality_gate.dto.grandstand;

import com.f1gp.f1_quality_gate.model.enums.Category;

public record GrandstandResponse(
        Long id,
        String name,
        String location,
        Category category,
        int capacity,
        double basePrice,
        boolean covered
) {
}
