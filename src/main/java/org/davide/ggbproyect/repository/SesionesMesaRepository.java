package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.SesionesMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SesionesMesaRepository extends JpaRepository<SesionesMesa, Integer> {

    @Query("SELECT e FROM SesionesMesa e WHERE " +
           "(:idMesa IS NULL OR e.idMesa.id = :idMesa) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado) AND " +
           "(:idReserva IS NULL OR e.idReserva.id = :idReserva) AND " +
           "(:idEmpleadoApertura IS NULL OR e.idEmpleadoApertura.id = :idEmpleadoApertura)")
    List<SesionesMesa> filter(@Param("idMesa") Integer idMesa,
                              @Param("estado") String estado,
                              @Param("idReserva") Integer idReserva,
                              @Param("idEmpleadoApertura") Integer idEmpleadoApertura);
}