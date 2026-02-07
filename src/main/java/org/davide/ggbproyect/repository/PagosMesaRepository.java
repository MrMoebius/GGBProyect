package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.PagosMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagosMesaRepository extends JpaRepository<PagosMesa, Integer> {

    @Query("SELECT e FROM PagosMesa e WHERE " +
           "(:idSesion IS NULL OR e.idSesion.id = :idSesion) AND " +
           "(:metodoPago IS NULL OR str(e.metodoPago) = :metodoPago) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado)")
    List<PagosMesa> filter(@Param("idSesion") Integer idSesion,
                           @Param("metodoPago") String metodoPago,
                           @Param("estado") String estado);
}