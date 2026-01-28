package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.JuegosCopia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegosCopiaRepository extends JpaRepository<JuegosCopia, Integer> {
}