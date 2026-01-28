package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.LineasComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineasComandaRepository extends JpaRepository<LineasComanda, Integer> {
}