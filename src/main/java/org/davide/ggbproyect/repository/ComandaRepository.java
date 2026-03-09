package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.Comanda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Integer> {

    @Override
    @EntityGraph(attributePaths = {"idSesion"})
    Page<Comanda> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"idSesion"})
    @Query("SELECT e FROM Comanda e WHERE " +
           "(:idSesion IS NULL OR e.idSesion.id = :idSesion) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado)")
    Page<Comanda> filter(@Param("idSesion") Integer idSesion,
                         @Param("estado") String estado,
                         Pageable pageable);

    List<Comanda> findByIdSesionId(Integer idSesion);
}