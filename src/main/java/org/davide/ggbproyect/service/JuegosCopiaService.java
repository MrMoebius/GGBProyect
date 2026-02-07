package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Juego;
import org.davide.ggbproyect.models.JuegosCopia;
import org.davide.ggbproyect.models.JuegosCopiaDTO;
import org.davide.ggbproyect.models.enums.EstadoCopiaJuego;
import org.davide.ggbproyect.repository.JuegosCopiaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JuegosCopiaService {

    private final JuegosCopiaRepository juegosCopiaRepository;

    public JuegosCopiaService(JuegosCopiaRepository juegosCopiaRepository) {
        this.juegosCopiaRepository = juegosCopiaRepository;
    }

    public List<JuegosCopiaDTO> getAll() {
        return juegosCopiaRepository.findAll().stream()
                .map(JuegosCopiaDTO::new)
                .collect(Collectors.toList());
    }

    public JuegosCopiaDTO getById(Integer id) {
        return juegosCopiaRepository.findById(id)
                .map(JuegosCopiaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Copia de juego con id " + id + " no encontrada"));
    }

    public JuegosCopiaDTO create(JuegosCopiaDTO juegosCopiaDTO) {
        JuegosCopia juegosCopia = juegosCopiaDTO.toEntity();
        return new JuegosCopiaDTO(juegosCopiaRepository.save(juegosCopia));
    }

    public JuegosCopiaDTO update(Integer id, JuegosCopiaDTO juegosCopiaDTO) {
        JuegosCopia existingCopia = juegosCopiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Copia de juego con id " + id + " no encontrada"));
        if (juegosCopiaDTO.getIdJuego() != null) {
            Juego juego = new Juego();
            juego.setId(juegosCopiaDTO.getIdJuego());
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