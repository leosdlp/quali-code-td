package com.f1gp.f1_quality_gate.repository;

import com.f1gp.f1_quality_gate.model.entity.Grandstand;
import com.f1gp.f1_quality_gate.model.enums.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrandstandRepository extends JpaRepository<Grandstand, Long> {

    List<Grandstand> findByCategory(Category category);
}
