package com.f1gp.f1_quality_gate.model.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void shouldContainExpectedValues() {
        assertThat(Category.values())
                .containsExactly(Category.BRONZE, Category.SILVER, Category.GOLD, Category.PLATINUM);
    }
}
