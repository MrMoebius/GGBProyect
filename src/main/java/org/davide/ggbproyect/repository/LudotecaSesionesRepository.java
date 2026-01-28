package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.LudotecaSesiones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LudotecaSesionesRepository extends JpaRepository<LudotecaSesiones, Integer> {
}