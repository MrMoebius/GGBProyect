package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByEmail(String email);

    @Query("SELECT e FROM Cliente e WHERE " +
           "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:telefono IS NULL OR LOWER(e.telefono) LIKE LOWER(CONCAT('%', :telefono, '%')))")
    List<Cliente> filter(@Param("nombre") String nombre,
                         @Param("email") String email,
                         @Param("telefono") String telefono);
}
