package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.PeticionesPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeticionesPagoRepository extends JpaRepository<PeticionesPago, Integer> {
}