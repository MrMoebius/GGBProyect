package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
}
