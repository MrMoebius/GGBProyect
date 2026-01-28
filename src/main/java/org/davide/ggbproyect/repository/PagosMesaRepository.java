package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.PagosMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagosMesaRepository extends JpaRepository<PagosMesa, Integer> {
}