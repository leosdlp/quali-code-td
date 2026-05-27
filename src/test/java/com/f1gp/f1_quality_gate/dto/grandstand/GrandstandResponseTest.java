package com.f1gp.f1_quality_gate.dto.grandstand;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.Category;
import org.junit.jupiter.api.Test;

class GrandstandResponseTest {

    @Test
    void shouldCreateGrandstandResponse() {
        GrandstandResponse response = new GrandstandResponse(
                1L,
                "Tribune Sainte-Beaume",
                "Virage 3",
                Category.GOLD,
                200,
                180.0,
                true
        );

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Tribune Sainte-Beaume");
        assertThat(response.location()).isEqualTo("Virage 3");
        assertThat(response.category()).isEqualTo(Category.GOLD);
        assertThat(response.capacity()).isEqualTo(200);
        assertThat(response.basePrice()).isEqualTo(180.0);
        assertThat(response.covered()).isTrue();
    }
}
