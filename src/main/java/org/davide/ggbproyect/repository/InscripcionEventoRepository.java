package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.InscripcionEvento;
import org.davide.ggbproyect.models.enums.EstadoInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionEventoRepository extends JpaRepository<InscripcionEvento, Integer> {

    List<InscripcionEvento> findByIdEventoId(Integer eventoId);

    List<InscripcionEvento> findByEmailUsuario(String email);

    Optional<InscripcionEvento> findByIdEventoIdAndEmailUsuario(Integer eventoId, String email);

    long countByIdEventoIdAndEstado(Integer eventoId, EstadoInscripcion estado);

    Optional<InscripcionEvento> findFirstByIdEventoIdAndEstadoOrderByFechaInscripcionAsc(
            Integer eventoId, EstadoInscripcion estado);
}
