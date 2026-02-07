package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.JuegosCopia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuegosCopiaRepository extends JpaRepository<JuegosCopia, Integer> {

    @Query("SELECT e FROM JuegosCopia e WHERE " +
           "(:idJuego IS NULL OR e.idJuego.id = :idJuego) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado)")
    List<JuegosCopia> filter(@Param("idJuego") Integer idJuego,
                             @Param("estado") String estado);
}