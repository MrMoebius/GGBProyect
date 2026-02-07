package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.RolesEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesEmpleadoRepository extends JpaRepository<RolesEmpleado, Integer> {

    @Query("SELECT e FROM RolesEmpleado e WHERE " +
           "(:nombreRol IS NULL OR LOWER(e.nombreRol) LIKE LOWER(CONCAT('%', :nombreRol, '%')))")
    List<RolesEmpleado> filter(@Param("nombreRol") String nombreRol);
}