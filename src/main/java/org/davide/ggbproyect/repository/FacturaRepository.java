package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.Factura;
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
public interface FacturaRepository extends JpaRepository<Factura, Integer> {

    @Override
    @EntityGraph(attributePaths = {"idSesion", "idCliente"})
    Page<Factura> findAll(Pageable pageable);

    Optional<Factura> findByIdSesionId(Integer idSesion);

    List<Factura> findByIdClienteId(Integer idCliente);

    @Query("SELECT MAX(f.id) FROM Factura f")
    Optional<Integer> findMaxId();
}
