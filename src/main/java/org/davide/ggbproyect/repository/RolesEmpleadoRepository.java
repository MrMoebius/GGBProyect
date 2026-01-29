package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.RolesEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesEmpleadoRepository extends JpaRepository<RolesEmpleado, Integer> {
}