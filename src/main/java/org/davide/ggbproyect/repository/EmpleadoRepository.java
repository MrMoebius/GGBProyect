package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    Optional<Empleado> findByEmail(String email);
    boolean existsByIdRol_Id(Integer idRol);

    @Query("SELECT e FROM Empleado e WHERE " +
           "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:idRol IS NULL OR e.idRol.id = :idRol) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado)")
    List<Empleado> filter(@Param("nombre") String nombre,
                          @Param("email") String email,
                          @Param("idRol") Integer idRol,
                          @Param("estado") String estado);
}
