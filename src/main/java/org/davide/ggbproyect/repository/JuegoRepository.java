package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Integer> {

    @Query("SELECT e FROM Juego e WHERE " +
           "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:complejidad IS NULL OR str(e.complejidad) = :complejidad) AND " +
           "(:idioma IS NULL OR str(e.idioma) = :idioma) AND " +
           "(:ubicacion IS NULL OR str(e.ubicacion) = :ubicacion) AND " +
           "(:activo IS NULL OR e.activo = :activo) AND " +
           "(:recomendadoDosJugadores IS NULL OR e.recomendadoDosJugadores = :recomendadoDosJugadores)")
    List<Juego> filter(@Param("nombre") String nombre,
                       @Param("complejidad") String complejidad,
                       @Param("idioma") String idioma,
                       @Param("ubicacion") String ubicacion,
                       @Param("activo") Boolean activo,
                       @Param("recomendadoDosJugadores") Boolean recomendadoDosJugadores);
}