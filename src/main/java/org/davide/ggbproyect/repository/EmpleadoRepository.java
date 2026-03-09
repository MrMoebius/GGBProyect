package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    @Override
    @EntityGraph(attributePaths = {"idRol"})
    Page<Empleado> findAll(Pageable pageable);
    @Query("SELECT e FROM Empleado e JOIN FETCH e.idRol WHERE e.email = :email")
    Optional<Empleado> findByEmail(@Param("email") String email);
    boolean existsByIdRol_Id(Integer idRol);

    @EntityGraph(attributePaths = {"idRol"})
    @Query("SELECT e FROM Empleado e WHERE " +
           "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:idRol IS NULL OR e.idRol.id = :idRol) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado)")
    Page<Empleado> filter(@Param("nombre") String nombre,
                          @Param("email") String email,
                          @Param("idRol") Integer idRol,
                          @Param("estado") String estado,
                          Pageable pageable);
}
