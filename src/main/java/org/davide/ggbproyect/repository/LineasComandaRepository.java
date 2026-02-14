package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.LineasComanda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineasComandaRepository extends JpaRepository<LineasComanda, Integer> {

    @Override
    @EntityGraph(attributePaths = {"idComanda", "idProducto"})
    Page<LineasComanda> findAll(Pageable pageable);

    @Query("SELECT e FROM LineasComanda e LEFT JOIN FETCH e.idComanda LEFT JOIN FETCH e.idProducto WHERE " +
           "(:idComanda IS NULL OR e.idComanda.id = :idComanda) AND " +
           "(:idProducto IS NULL OR e.idProducto.id = :idProducto)")
    Page<LineasComanda> filter(@Param("idComanda") Integer idComanda,
                               @Param("idProducto") Integer idProducto,
                               Pageable pageable);

    List<LineasComanda> findByIdComandaId(Integer idComanda);
}