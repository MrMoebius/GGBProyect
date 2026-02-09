package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Juego;
import org.davide.ggbproyect.models.JuegosCopia;
import org.davide.ggbproyect.models.JuegosCopiaDTO;
import org.davide.ggbproyect.models.enums.EstadoCopiaJuego;
import org.davide.ggbproyect.repository.JuegoRepository;
import org.davide.ggbproyect.repository.JuegosCopiaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JuegosCopiaService {

    private final JuegosCopiaRepository juegosCopiaRepository;
    private final JuegoRepository juegoRepository;

    public JuegosCopiaService(JuegosCopiaRepository juegosCopiaRepository,
                              JuegoRepository juegoRepository) {
        this.juegosCopiaRepository = juegosCopiaRepository;
        this.juegoRepository = juegoRepository;
    }

    public Page<JuegosCopiaDTO> getAll(Pageable pageable) {
        return juegosCopiaRepository.findAll(pageable)
                .map(JuegosCopiaDTO::new);
    }

    public JuegosCopiaDTO getById(Integer id) {
        return juegosCopiaRepository.findById(id)
                .map(JuegosCopiaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Copia de juego con id " + id + " no encontrada"));
    }

    public JuegosCopiaDTO create(JuegosCopiaDTO juegosCopiaDTO) {
        JuegosCopia juegosCopia = juegosCopiaDTO.toEntity();
        if (juegosCopiaDTO.getIdJuego() != null) {
            Juego juego = juegoRepository.findById(juegosCopiaDTO.getIdJuego())
                    .orElseThrow(() -> new EntityNotFoundException("Juego con id " + juegosCopiaDTO.getIdJuego() + " no encontrado"));
            juegosCopia.setIdJuego(juego);
        }
        return new JuegosCopiaDTO(juegosCopiaRepository.save(juegosCopia));
    }

    public JuegosCopiaDTO update(Integer id, JuegosCopiaDTO juegosCopiaDTO) {
        JuegosCopia existingCopia = juegosCopiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Copia de juego con id " + id + " no encontrada"));
        if (juegosCopiaDTO.getIdJuego() != null) {
            Juego juego = juegoRepository.findById(juegosCopiaDTO.getIdJuego())
                    .orElseThrow(() -> new EntityNotFoundException("Juego con id " + juegosCopiaDTO.getIdJuego() + " no encontrado"));
            existingCopia.setIdJuego(juego);
        }
        existingCopia.setCodigoInterno(juegosCopiaDTO.getCodigoInterno());
        if (juegosCopiaDTO.getEstado() != null) {
            try {
                existingCopia.setEstado(EstadoCopiaJuego.valueOf(juegosCopiaDTO.getEstado()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de estado invalido: " + juegosCopiaDTO.getEstado());
            }
        }
        return new JuegosCopiaDTO(juegosCopiaRepository.save(existingCopia));
    }

    public List<JuegosCopiaDTO> filter(Integer idJuego, String estado) {
        return juegosCopiaRepository.filter(idJuego, estado)
                .stream()
                .map(JuegosCopiaDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!juegosCopiaRepository.existsById(id)) {
            throw new EntityNotFoundException("Copia de juego con id " + id + " no encontrada");
        }
        juegosCopiaRepository.deleteById(id);
    }
}