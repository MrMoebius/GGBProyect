package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.ReservasJuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservasJuegoRepository extends JpaRepository<ReservasJuego, Integer> {
}