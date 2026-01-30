package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Juego;
import org.davide.ggbproyect.models.JuegosCopia;
import org.davide.ggbproyect.models.JuegosCopiaDTO;
import org.davide.ggbproyect.models.enums.EstadoCopiaJuego;
import org.davide.ggbproyect.repository.JuegosCopiaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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

    public Optional<JuegosCopiaDTO> getById(Integer id) {
        return juegosCopiaRepository.findById(id)
                .map(JuegosCopiaDTO::new);
    }

    public JuegosCopiaDTO create(JuegosCopiaDTO juegosCopiaDTO) {
        JuegosCopia juegosCopia = juegosCopiaDTO.toEntity();
        return new JuegosCopiaDTO(juegosCopiaRepository.save(juegosCopia));
    }

    public Optional<JuegosCopiaDTO> update(Integer id, JuegosCopiaDTO juegosCopiaDTO) {
        return juegosCopiaRepository.findById(id).map(existingCopia -> {
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
                    // Handle invalid enum
                }
            }
            return new JuegosCopiaDTO(juegosCopiaRepository.save(existingCopia));
        });
    }

    public boolean delete(Integer id) {
        if (juegosCopiaRepository.existsById(id)) {
            juegosCopiaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}