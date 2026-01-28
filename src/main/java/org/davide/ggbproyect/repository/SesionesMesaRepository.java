package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.SesionesMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SesionesMesaRepository extends JpaRepository<SesionesMesa, Integer> {
}