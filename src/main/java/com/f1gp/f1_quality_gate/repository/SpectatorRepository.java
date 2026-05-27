package com.f1gp.f1_quality_gate.repository;

import com.f1gp.f1_quality_gate.model.entity.Spectator;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpectatorRepository extends JpaRepository<Spectator, Long> {

    boolean existsByEmail(String email);

    Optional<Spectator> findByEmail(String email);
}
