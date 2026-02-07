package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.ReservasMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservasMesaRepository extends JpaRepository<ReservasMesa, Integer> {

    @Query("SELECT e FROM ReservasMesa e WHERE " +
           "(:idCliente IS NULL OR e.idCliente.id = :idCliente) AND " +
           "(:idMesa IS NULL OR e.idMesa.id = :idMesa) AND " +
           "(:idJuegoDeseado IS NULL OR e.idJuegoDeseado.id = :idJuegoDeseado) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado)")
    List<ReservasMesa> filter(@Param("idCliente") Integer idCliente,
                              @Param("idMesa") Integer idMesa,
                              @Param("idJuegoDeseado") Integer idJuegoDeseado,
                              @Param("estado") String estado);
}