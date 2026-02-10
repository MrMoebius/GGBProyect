package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.JuegosCopia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuegosCopiaRepository extends JpaRepository<JuegosCopia, Integer> {

    @Override
    @EntityGraph(attributePaths = {"idJuego"})
    Page<JuegosCopia> findAll(Pageable pageable);

    @Query("SELECT e FROM JuegosCopia e LEFT JOIN FETCH e.idJuego WHERE " +
           "(:idJuego IS NULL OR e.idJuego.id = :idJuego) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado)")
    Page<JuegosCopia> filter(@Param("idJuego") Integer idJuego,
                             @Param("estado") String estado,
                             Pageable pageable);
}