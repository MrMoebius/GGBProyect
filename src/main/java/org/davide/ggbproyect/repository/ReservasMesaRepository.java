package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.ReservasMesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservasMesaRepository extends JpaRepository<ReservasMesa, Integer> {

    @EntityGraph(attributePaths = {"idCliente", "idMesa", "idJuegoDeseado"})
    List<ReservasMesa> findByIdClienteId(Integer idCliente);

    @Override
    @EntityGraph(attributePaths = {"idCliente", "idMesa", "idJuegoDeseado"})
    Page<ReservasMesa> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"idCliente", "idMesa", "idJuegoDeseado"})
    @Query("SELECT e FROM ReservasMesa e WHERE " +
           "(:idCliente IS NULL OR e.idCliente.id = :idCliente) AND " +
           "(:idMesa IS NULL OR e.idMesa.id = :idMesa) AND " +
           "(:idJuegoDeseado IS NULL OR e.idJuegoDeseado.id = :idJuegoDeseado) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado)")
    Page<ReservasMesa> filter(@Param("idCliente") Integer idCliente,
                              @Param("idMesa") Integer idMesa,
                              @Param("idJuegoDeseado") Integer idJuegoDeseado,
                              @Param("estado") String estado,
                              Pageable pageable);
}