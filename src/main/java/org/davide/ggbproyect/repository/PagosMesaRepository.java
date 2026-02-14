package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.PagosMesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagosMesaRepository extends JpaRepository<PagosMesa, Integer> {

    @Override
    @EntityGraph(attributePaths = {"idSesion"})
    Page<PagosMesa> findAll(Pageable pageable);

    @Query("SELECT e FROM PagosMesa e LEFT JOIN FETCH e.idSesion WHERE " +
           "(:idSesion IS NULL OR e.idSesion.id = :idSesion) AND " +
           "(:metodoPago IS NULL OR str(e.metodoPago) = :metodoPago) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado)")
    Page<PagosMesa> filter(@Param("idSesion") Integer idSesion,
                           @Param("metodoPago") String metodoPago,
                           @Param("estado") String estado,
                           Pageable pageable);

    List<PagosMesa> findByIdSesionId(Integer idSesion);
}