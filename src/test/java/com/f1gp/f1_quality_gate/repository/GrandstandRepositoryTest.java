package com.f1gp.f1_quality_gate.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.f1gp.f1_quality_gate.model.entity.Grandstand;
import com.f1gp.f1_quality_gate.model.enums.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GrandstandRepositoryTest {

    @Autowired
    private GrandstandRepository grandstandRepository;

    @Test
    void findByCategory_shouldReturnMatchingGrandstands() {
        Grandstand bronzeGrandstand = new Grandstand(null, "Bronze Stand", "Turn 1", Category.BRONZE, 100, 80.0, false);
        Grandstand goldGrandstand = new Grandstand(null, "Gold Stand", "Turn 3", Category.GOLD, 200, 180.0, true);

        grandstandRepository.save(bronzeGrandstand);
        grandstandRepository.save(goldGrandstand);

        assertThat(grandstandRepository.findByCategory(Category.GOLD))
                .hasSize(1)
                .first()
                .extracting(Grandstand::getName)
                .isEqualTo("Gold Stand");
    }
}
