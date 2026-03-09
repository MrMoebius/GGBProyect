package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Juego;
import org.davide.ggbproyect.models.JuegoDTO;
import org.davide.ggbproyect.models.enums.ComplejidadJuego;
import org.davide.ggbproyect.models.enums.IdiomaJuego;
import org.davide.ggbproyect.models.enums.UbicacionJuego;
import org.davide.ggbproyect.repository.JuegoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JuegoService {

    private final JuegoRepository juegoRepository;

    public JuegoService(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }

    @Transactional(readOnly = true)
    public Page<JuegoDTO> getAll(Pageable pageable) {
        return juegoRepository.findAll(pageable)
                .map(JuegoDTO::new);
    }

    @Transactional(readOnly = true)
    public JuegoDTO getById(Integer id) {
        return juegoRepository.findById(id)
                .map(JuegoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Juego con id " + id + " no encontrado"));
    }

    public JuegoDTO create(JuegoDTO juegoDTO) {
        validarJugadores(juegoDTO);
        Juego juego = juegoDTO.toEntity();
        return new JuegoDTO(juegoRepository.save(juego));
    }

    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return juegoRepository.existsByNombre(nombre);
    }

    public JuegoDTO update(Integer id, JuegoDTO juegoDTO) {
        validarJugadores(juegoDTO);
        Juego existingJuego = juegoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Juego con id " + id + " no encontrado"));
        if (juegoRepository.existsByNombreAndIdNot(juegoDTO.getNombre(), id)) {
            throw new IllegalArgumentException("El nombre del juego esta duplicado ");
        }

        existingJuego.setNombre(juegoDTO.getNombre());
        existingJuego.setMinJugadores(juegoDTO.getMinJugadores());
        existingJuego.setMaxJugadores(juegoDTO.getMaxJugadores());
        existingJuego.setDuracionMediaMin(juegoDTO.getDuracionMediaMin());

        if (juegoDTO.getComplejidad() != null) {
            try {
                existingJuego.setComplejidad(ComplejidadJuego.valueOf(juegoDTO.getComplejidad()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de complejidad invalido: " + juegoDTO.getComplejidad());
            }
        }
        existingJuego.setGenero(juegoDTO.getGenero());
        if (juegoDTO.getIdioma() != null) {
            try {
                existingJuego.setIdioma(IdiomaJuego.valueOf(juegoDTO.getIdioma()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de idioma invalido: " + juegoDTO.getIdioma());
            }
        }
        existingJuego.setDescripcion(juegoDTO.getDescripcion());
        existingJuego.setObservaciones(juegoDTO.getObservaciones());
        if (juegoDTO.getUbicacion() != null) {
            try {
                existingJuego.setUbicacion(UbicacionJuego.valueOf(juegoDTO.getUbicacion()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de ubicacion invalido: " + juegoDTO.getUbicacion());
            }
        }
        existingJuego.setRecomendadoDosJugadores(juegoDTO.getRecomendadoDosJugadores());
        existingJuego.setActivo(juegoDTO.getActivo());
        return new JuegoDTO(juegoRepository.save(existingJuego));
    }

    private void validarJugadores(JuegoDTO dto) {
        if (dto.getMinJugadores() != null && dto.getMinJugadores() < 0) {
            throw new IllegalArgumentException("El minimo de jugadores no puede ser negativo");
        }
        if (dto.getMaxJugadores() != null && dto.getMaxJugadores() < 0) {
            throw new IllegalArgumentException("El maximo de jugadores no puede ser negativo");
        }
        if (dto.getMinJugadores() != null && dto.getMaxJugadores() != null
                && dto.getMinJugadores() > dto.getMaxJugadores()) {
            throw new IllegalArgumentException("El minimo de jugadores no puede ser mayor que el maximo de jugadores");
        }
    }

    @Transactional(readOnly = true)
    public Page<JuegoDTO> filter(String nombre, String complejidad, String idioma,
                                 String ubicacion, Boolean activo, Boolean recomendadoDosJugadores, Pageable pageable) {
        return juegoRepository.filter(nombre, complejidad, idioma, ubicacion, activo, recomendadoDosJugadores, pageable)
                .map(JuegoDTO::new);
    }

    public void delete(Integer id) {
        if (!juegoRepository.existsById(id)) {
            throw new EntityNotFoundException("Juego con id " + id + " no encontrado");
        }
        juegoRepository.deleteById(id);
    }
}