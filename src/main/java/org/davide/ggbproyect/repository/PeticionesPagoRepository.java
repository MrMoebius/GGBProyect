package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.PeticionesPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeticionesPagoRepository extends JpaRepository<PeticionesPago, Integer> {

    @Override
    @EntityGraph(attributePaths = {"idSesion"})
    Page<PeticionesPago> findAll(Pageable pageable);

    @Query("SELECT e FROM PeticionesPago e LEFT JOIN FETCH e.idSesion WHERE " +
           "(:idSesion IS NULL OR e.idSesion.id = :idSesion) AND " +
           "(:metodoPreferido IS NULL OR str(e.metodoPreferido) = :metodoPreferido) AND " +
           "(:atendida IS NULL OR e.atendida = :atendida)")
    Page<PeticionesPago> filter(@Param("idSesion") Integer idSesion,
                                @Param("metodoPreferido") String metodoPreferido,
                                @Param("atendida") Boolean atendida,
                                Pageable pageable);
}