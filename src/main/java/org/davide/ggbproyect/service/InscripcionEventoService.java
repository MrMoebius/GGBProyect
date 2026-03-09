package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Evento;
import org.davide.ggbproyect.models.InscripcionEvento;
import org.davide.ggbproyect.models.InscripcionEventoDTO;
import org.davide.ggbproyect.models.enums.EstadoInscripcion;
import org.davide.ggbproyect.repository.EventoRepository;
import org.davide.ggbproyect.repository.InscripcionEventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InscripcionEventoService {

    private final InscripcionEventoRepository inscripcionRepository;
    private final EventoRepository eventoRepository;

    public InscripcionEventoService(InscripcionEventoRepository inscripcionRepository,
                                    EventoRepository eventoRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.eventoRepository = eventoRepository;
    }

    public InscripcionEventoDTO inscribirse(Integer eventoId, String email) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento con id " + eventoId + " no encontrado"));

        Optional<InscripcionEvento> existente = inscripcionRepository
                .findByIdEventoIdAndEmailUsuario(eventoId, email);
        if (existente.isPresent() && existente.get().getEstado() != EstadoInscripcion.CANCELADA) {
            throw new IllegalStateException("Ya estas inscrito en este evento");
        }

        long confirmadas = inscripcionRepository.countByIdEventoIdAndEstado(
                eventoId, EstadoInscripcion.CONFIRMADA);

        InscripcionEvento inscripcion;
        if (existente.isPresent()) {
            inscripcion = existente.get();
        } else {
            inscripcion = new InscripcionEvento();
            inscripcion.setIdEvento(evento);
            inscripcion.setEmailUsuario(email);
        }

        inscripcion.setEstado(confirmadas < evento.getCapacidad()
                ? EstadoInscripcion.CONFIRMADA
                : EstadoInscripcion.LISTA_ESPERA);
        inscripcion.setFechaInscripcion(Instant.now());

        return new InscripcionEventoDTO(inscripcionRepository.save(inscripcion));
    }

    public void desinscribirse(Integer eventoId, String email) {
        InscripcionEvento inscripcion = inscripcionRepository
                .findByIdEventoIdAndEmailUsuario(eventoId, email)
                .orElseThrow(() -> new EntityNotFoundException("No estas inscrito en este evento"));

        if (inscripcion.getEstado() == EstadoInscripcion.CANCELADA) {
            throw new IllegalStateException("La inscripcion ya esta cancelada");
        }

        boolean eraConfirmada = inscripcion.getEstado() == EstadoInscripcion.CONFIRMADA;
        inscripcion.setEstado(EstadoInscripcion.CANCELADA);
        inscripcionRepository.save(inscripcion);

        if (eraConfirmada) {
            inscripcionRepository
                    .findFirstByIdEventoIdAndEstadoOrderByFechaInscripcionAsc(
                            eventoId, EstadoInscripcion.LISTA_ESPERA)
                    .ifPresent(siguiente -> {
                        siguiente.setEstado(EstadoInscripcion.CONFIRMADA);
                        inscripcionRepository.save(siguiente);
                    });
        }
    }

    @Transactional(readOnly = true)
    public List<InscripcionEventoDTO> getByEvento(Integer eventoId) {
        return inscripcionRepository.findByIdEventoId(eventoId)
                .stream()
                .map(InscripcionEventoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InscripcionEventoDTO> getMisInscripciones(String email) {
        return inscripcionRepository.findByEmailUsuario(email)
                .stream()
                .map(InscripcionEventoDTO::new)
                .collect(Collectors.toList());
    }
}
