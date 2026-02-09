package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Juego;
import org.davide.ggbproyect.models.JuegoDTO;
import org.davide.ggbproyect.models.enums.ComplejidadJuego;
import org.davide.ggbproyect.models.enums.IdiomaJuego;
import org.davide.ggbproyect.models.enums.UbicacionJuego;
import org.davide.ggbproyect.repository.JuegoRepository;
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

    public List<JuegoDTO> getAll() {
        return juegoRepository.findAll().stream()
                .map(JuegoDTO::new)
                .collect(Collectors.toList());
    }

    public JuegoDTO getById(Integer id) {
        return juegoRepository.findById(id)
                .map(JuegoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Juego con id " + id + " no encontrado"));
    }

    public JuegoDTO create(JuegoDTO juegoDTO) {
        Juego juego = juegoDTO.toEntity();
        return new JuegoDTO(juegoRepository.save(juego));
    }

    public JuegoDTO update(Integer id, JuegoDTO juegoDTO) {
        Juego existingJuego = juegoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Juego con id " + id + " no encontrado"));
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

    public List<JuegoDTO> filter(String nombre, String complejidad, String idioma,
                                 String ubicacion, Boolean activo, Boolean recomendadoDosJugadores) {
        return juegoRepository.filter(nombre, complejidad, idioma, ubicacion, activo, recomendadoDosJugadores)
                .stream()
                .map(JuegoDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!juegoRepository.existsById(id)) {
            throw new EntityNotFoundException("Juego con id " + id + " no encontrado");
        }
        juegoRepository.deleteById(id);
    }
}