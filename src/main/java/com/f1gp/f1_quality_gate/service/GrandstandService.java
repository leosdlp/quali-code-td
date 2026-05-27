package com.f1gp.f1_quality_gate.service;

import com.f1gp.f1_quality_gate.dto.grandstand.GrandstandRequest;
import com.f1gp.f1_quality_gate.dto.grandstand.GrandstandResponse;
import com.f1gp.f1_quality_gate.model.entity.Grandstand;
import com.f1gp.f1_quality_gate.model.enums.Category;
import com.f1gp.f1_quality_gate.repository.GrandstandRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GrandstandService {

    private final GrandstandRepository grandstandRepository;

    public GrandstandService(GrandstandRepository grandstandRepository) {
        this.grandstandRepository = grandstandRepository;
    }

    public GrandstandResponse createGrandstand(GrandstandRequest request) {
        Grandstand grandstand = new Grandstand(
                null,
                request.name(),
                request.location(),
                request.category(),
                request.capacity(),
                request.basePrice(),
                request.covered()
        );

        return toResponse(grandstandRepository.save(grandstand));
    }

    public List<GrandstandResponse> getGrandstands(Category category) {
        List<Grandstand> grandstands = category == null
                ? grandstandRepository.findAll()
                : grandstandRepository.findByCategory(category);

        return grandstands.stream()
                .map(this::toResponse)
                .toList();
    }

    private GrandstandResponse toResponse(Grandstand grandstand) {
        return new GrandstandResponse(
                grandstand.getId(),
                grandstand.getName(),
                grandstand.getLocation(),
                grandstand.getCategory(),
                grandstand.getCapacity(),
                grandstand.getBasePrice(),
                grandstand.isCovered()
        );
    }
}
