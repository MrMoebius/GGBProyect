package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.LineasComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineasComandaRepository extends JpaRepository<LineasComanda, Integer> {

    @Query("SELECT e FROM LineasComanda e WHERE " +
           "(:idComanda IS NULL OR e.idComanda.id = :idComanda) AND " +
           "(:idProducto IS NULL OR e.idProducto.id = :idProducto)")
    List<LineasComanda> filter(@Param("idComanda") Integer idComanda,
                               @Param("idProducto") Integer idProducto);
}