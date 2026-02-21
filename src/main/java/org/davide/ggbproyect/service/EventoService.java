package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Evento;
import org.davide.ggbproyect.models.EventoDTO;
import org.davide.ggbproyect.models.enums.EstadoInscripcion;
import org.davide.ggbproyect.repository.EventoRepository;
import org.davide.ggbproyect.repository.InscripcionEventoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class EventoService {

    private final EventoRepository eventoRepository;
    private final InscripcionEventoRepository inscripcionRepository;

    public EventoService(EventoRepository eventoRepository,
                         InscripcionEventoRepository inscripcionRepository) {
        this.eventoRepository = eventoRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    @Transactional(readOnly = true)
    public Page<EventoDTO> getAll(Pageable pageable) {
        return eventoRepository.findAll(pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public EventoDTO getById(Integer id) {
        return eventoRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Evento con id " + id + " no encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<EventoDTO> filter(String tipo, String estado, Pageable pageable) {
        return eventoRepository.filter(tipo, estado, pageable)
                .map(this::toDTO);
    }

    public EventoDTO create(EventoDTO dto) {
        Evento entity = dto.toEntity();
        return toDTO(eventoRepository.save(entity));
    }

    public EventoDTO update(Integer id, EventoDTO dto) {
        Evento existing = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento con id " + id + " no encontrado"));

        existing.setTitulo(dto.getTitulo());
        existing.setDescripcion(dto.getDescripcion());
        existing.setFecha(dto.getFecha());
        existing.setHora(dto.getHora());
        existing.setHoraFin(dto.getHoraFin());
        existing.setUbicacion(dto.getUbicacion());
        existing.setCapacidad(dto.getCapacidad());
        if (dto.getTipo() != null) {
            existing.setTipo(org.davide.ggbproyect.models.enums.TipoEvento.valueOf(dto.getTipo()));
        }
        if (dto.getEstado() != null) {
            existing.setEstado(org.davide.ggbproyect.models.enums.EstadoEvento.valueOf(dto.getEstado()));
        }
        existing.setTags(dto.getTags() != null ? String.join(",", dto.getTags()) : null);
        existing.setCreadoPor(dto.getCreadoPor());

        return toDTO(eventoRepository.save(existing));
    }

    public void delete(Integer id) {
        if (!eventoRepository.existsById(id)) {
            throw new EntityNotFoundException("Evento con id " + id + " no encontrado");
        }
        inscripcionRepository.findByIdEventoId(id)
                .forEach(i -> inscripcionRepository.delete(i));
        eventoRepository.deleteById(id);
    }

    private EventoDTO toDTO(Evento entity) {
        EventoDTO dto = new EventoDTO(entity);
        dto.setInscritos((int) inscripcionRepository.countByIdEventoIdAndEstado(
                entity.getId(), EstadoInscripcion.CONFIRMADA));
        dto.setListaEspera((int) inscripcionRepository.countByIdEventoIdAndEstado(
                entity.getId(), EstadoInscripcion.LISTA_ESPERA));
        return dto;
    }
}
