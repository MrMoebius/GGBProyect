package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.LudotecaSesiones;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LudotecaSesionesRepository extends JpaRepository<LudotecaSesiones, Integer> {

    @Override
    @EntityGraph(attributePaths = {"idSesion", "idComandaLudoteca"})
    Page<LudotecaSesiones> findAll(Pageable pageable);

    @Query("SELECT e FROM LudotecaSesiones e LEFT JOIN FETCH e.idSesion LEFT JOIN FETCH e.idComandaLudoteca WHERE " +
           "(:idSesion IS NULL OR e.idSesion.id = :idSesion)")
    Page<LudotecaSesiones> filter(@Param("idSesion") Integer idSesion,
                                   Pageable pageable);
}