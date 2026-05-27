package com.f1gp.f1_quality_gate.model.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.enums.Category;
import org.junit.jupiter.api.Test;

class GrandstandTest {

    @Test
    void shouldCreateGrandstandWithNoArgsConstructor() {
        Grandstand grandstand = new Grandstand();

        assertThat(grandstand).isNotNull();
    }

    @Test
    void shouldCreateGrandstandWithAllArgsConstructor() {
        Grandstand grandstand = new Grandstand(
                1L,
                "Tribune Sainte-Beaume",
                "Virage 3",
                Category.GOLD,
                200,
                180.0,
                true
        );

        assertThat(grandstand.getId()).isEqualTo(1L);
        assertThat(grandstand.getName()).isEqualTo("Tribune Sainte-Beaume");
        assertThat(grandstand.getLocation()).isEqualTo("Virage 3");
        assertThat(grandstand.getCategory()).isEqualTo(Category.GOLD);
        assertThat(grandstand.getCapacity()).isEqualTo(200);
        assertThat(grandstand.getBasePrice()).isEqualTo(180.0);
        assertThat(grandstand.isCovered()).isTrue();
    }

    @Test
    void shouldUpdateGrandstandWithSetters() {
        Grandstand grandstand = new Grandstand();

        grandstand.setId(2L);
        grandstand.setName("Tribune Le Beausset");
        grandstand.setLocation("Ligne droite");
        grandstand.setCategory(Category.PLATINUM);
        grandstand.setCapacity(500);
        grandstand.setBasePrice(250.0);
        grandstand.setCovered(false);

        assertThat(grandstand.getId()).isEqualTo(2L);
        assertThat(grandstand.getName()).isEqualTo("Tribune Le Beausset");
        assertThat(grandstand.getLocation()).isEqualTo("Ligne droite");
        assertThat(grandstand.getCategory()).isEqualTo(Category.PLATINUM);
        assertThat(grandstand.getCapacity()).isEqualTo(500);
        assertThat(grandstand.getBasePrice()).isEqualTo(250.0);
        assertThat(grandstand.isCovered()).isFalse();
    }
}
