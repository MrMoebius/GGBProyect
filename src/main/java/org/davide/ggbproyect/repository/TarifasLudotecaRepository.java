package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.TarifasLudoteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarifasLudotecaRepository extends JpaRepository<TarifasLudoteca, Integer> {

    @Query("SELECT e FROM TarifasLudoteca e WHERE " +
           "(:nombreTramo IS NULL OR LOWER(e.nombreTramo) LIKE LOWER(CONCAT('%', :nombreTramo, '%'))) AND " +
           "(:activo IS NULL OR e.activo = :activo)")
    List<TarifasLudoteca> filter(@Param("nombreTramo") String nombreTramo,
                                 @Param("activo") Boolean activo);
}