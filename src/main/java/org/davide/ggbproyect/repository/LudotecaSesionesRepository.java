package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.LudotecaSesiones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LudotecaSesionesRepository extends JpaRepository<LudotecaSesiones, Integer> {

    @Query("SELECT e FROM LudotecaSesiones e WHERE " +
           "(:idSesion IS NULL OR e.idSesion.id = :idSesion)")
    List<LudotecaSesiones> filter(@Param("idSesion") Integer idSesion);
}