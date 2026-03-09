package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {

    @Query("SELECT e FROM Evento e WHERE " +
           "(:tipo IS NULL OR str(e.tipo) = :tipo) AND " +
           "(:estado IS NULL OR str(e.estado) = :estado)")
    Page<Evento> filter(@Param("tipo") String tipo,
                        @Param("estado") String estado,
                        Pageable pageable);
}
