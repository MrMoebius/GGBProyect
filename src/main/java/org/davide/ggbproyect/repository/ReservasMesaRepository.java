package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.ReservasMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservasMesaRepository extends JpaRepository<ReservasMesa, Integer> {
}