package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.Mesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Integer> {

    @Query("SELECT e FROM Mesa e WHERE " +
           "(:nombreMesa IS NULL OR LOWER(e.nombreMesa) LIKE LOWER(CONCAT('%', :nombreMesa, '%'))) AND " +
           "(:zona IS NULL OR LOWER(e.zona) LIKE LOWER(CONCAT('%', :zona, '%'))) AND " +
           "(:ubicacion IS NULL OR str(e.ubicacion) = :ubicacion) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado) AND " +
           "(:capacidad IS NULL OR e.capacidad = :capacidad)")
    Page<Mesa> filter(@Param("nombreMesa") String nombreMesa,
                      @Param("zona") String zona,
                      @Param("ubicacion") String ubicacion,
                      @Param("estado") String estado,
                      @Param("capacidad") Integer capacidad,
                      Pageable pageable);

    boolean existsByNombreMesa(String nombreMesa);

    boolean existsByNombreMesaAndIdNot(String nombreMesa, Integer id);

    boolean existsByNumeroMesa(Integer numeroMesa);

    boolean existsByNumeroMesaAndIdNot(Integer numeroMesa, Integer id);
}