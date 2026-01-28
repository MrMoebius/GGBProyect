package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.TarifasLudoteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifasLudotecaRepository extends JpaRepository<TarifasLudoteca, Integer> {
}