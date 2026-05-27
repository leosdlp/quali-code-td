package com.f1gp.f1_quality_gate.repository;

import com.f1gp.f1_quality_gate.model.entity.RaceSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceSessionRepository extends JpaRepository<RaceSession, Long> {
}
