package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("SELECT e FROM Producto e WHERE " +
           "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:categoria IS NULL OR LOWER(e.categoria) LIKE LOWER(CONCAT('%', :categoria, '%'))) AND " +
           "(:activo IS NULL OR e.activo = :activo)")
    List<Producto> filter(@Param("nombre") String nombre,
                          @Param("categoria") String categoria,
                          @Param("activo") Boolean activo);
}