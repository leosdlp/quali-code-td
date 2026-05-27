package com.f1gp.f1_quality_gate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.f1gp.f1_quality_gate.dto.grandstand.GrandstandRequest;
import com.f1gp.f1_quality_gate.dto.grandstand.GrandstandResponse;
import com.f1gp.f1_quality_gate.model.entity.Grandstand;
import com.f1gp.f1_quality_gate.model.enums.Category;
import com.f1gp.f1_quality_gate.repository.GrandstandRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GrandstandServiceTest {

    @Mock
    private GrandstandRepository grandstandRepository;

    @InjectMocks
    private GrandstandService grandstandService;

    @Test
    void createGrandstand_shouldSaveAndReturnGrandstand() {
        GrandstandRequest request = new GrandstandRequest(
                "Tribune Sainte-Beaume",
                "Virage 3",
                Category.GOLD,
                200,
                180.0,
                true
        );

        Grandstand savedGrandstand = new Grandstand(
                1L,
                "Tribune Sainte-Beaume",
                "Virage 3",
                Category.GOLD,
                200,
                180.0,
                true
        );

        when(grandstandRepository.save(any(Grandstand.class))).thenReturn(savedGrandstand);

        GrandstandResponse response = grandstandService.createGrandstand(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Tribune Sainte-Beaume");
        assertThat(response.category()).isEqualTo(Category.GOLD);
        verify(grandstandRepository).save(any(Grandstand.class));
    }

    @Test
    void getGrandstands_shouldReturnAllGrandstandsWhenCategoryIsNull() {
        when(grandstandRepository.findAll()).thenReturn(List.of(
                new Grandstand(1L, "Bronze Stand", "Turn 1", Category.BRONZE, 100, 80.0, false),
                new Grandstand(2L, "Gold Stand", "Turn 3", Category.GOLD, 200, 180.0, true)
        ));

        List<GrandstandResponse> responses = grandstandService.getGrandstands(null);

        assertThat(responses).hasSize(2);
        verify(grandstandRepository).findAll();
    }

    @Test
    void getGrandstands_shouldFilterByCategoryWhenCategoryIsProvided() {
        when(grandstandRepository.findByCategory(Category.GOLD)).thenReturn(List.of(
                new Grandstand(2L, "Gold Stand", "Turn 3", Category.GOLD, 200, 180.0, true)
        ));

        List<GrandstandResponse> responses = grandstandService.getGrandstands(Category.GOLD);

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().category()).isEqualTo(Category.GOLD);
        verify(grandstandRepository).findByCategory(Category.GOLD);
    }
}
