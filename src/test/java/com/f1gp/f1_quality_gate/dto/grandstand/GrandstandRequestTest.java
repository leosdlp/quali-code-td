package com.f1gp.f1_quality_gate.dto.grandstand;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.Category;
import org.junit.jupiter.api.Test;

class GrandstandRequestTest {

    @Test
    void shouldCreateGrandstandRequest() {
        GrandstandRequest request = new GrandstandRequest(
                "Tribune Sainte-Beaume",
                "Virage 3",
                Category.GOLD,
                200,
                180.0,
                true
        );

        assertThat(request.name()).isEqualTo("Tribune Sainte-Beaume");
        assertThat(request.location()).isEqualTo("Virage 3");
        assertThat(request.category()).isEqualTo(Category.GOLD);
        assertThat(request.capacity()).isEqualTo(200);
        assertThat(request.basePrice()).isEqualTo(180.0);
        assertThat(request.covered()).isTrue();
    }
}
