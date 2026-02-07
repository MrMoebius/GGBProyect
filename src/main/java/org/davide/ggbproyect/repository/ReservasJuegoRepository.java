package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.ReservasJuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservasJuegoRepository extends JpaRepository<ReservasJuego, Integer> {

    @Query("SELECT e FROM ReservasJuego e WHERE " +
           "(:idSesion IS NULL OR e.idSesion.id = :idSesion) AND " +
           "(:idCopia IS NULL OR e.idCopia.id = :idCopia) AND " +
           "(:estado IS NULL OR LOWER(e.estado) LIKE LOWER(CONCAT('%', :estado, '%')))")
    List<ReservasJuego> filter(@Param("idSesion") Integer idSesion,
                               @Param("idCopia") Integer idCopia,
                               @Param("estado") String estado);
}