package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Juego;
import org.davide.ggbproyect.models.JuegoDTO;
import org.davide.ggbproyect.repository.JuegoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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

    public Optional<JuegoDTO> getById(Integer id) {
        return juegoRepository.findById(id)
                .map(JuegoDTO::new);
    }

    public JuegoDTO create(JuegoDTO juegoDTO) {
        Juego juego = juegoDTO.toEntity();
        return new JuegoDTO(juegoRepository.save(juego));
    }

    public Optional<JuegoDTO> update(Integer id, JuegoDTO juegoDTO) {
        return juegoRepository.findById(id).map(existingJuego -> {
            existingJuego.setNombre(juegoDTO.getNombre());
            existingJuego.setMinJugadores(juegoDTO.getMinJugadores());
            existingJuego.setMaxJugadores(juegoDTO.getMaxJugadores());
            existingJuego.setDuracionMediaMin(juegoDTO.getDuracionMediaMin());
            existingJuego.setComplejidad(juegoDTO.getComplejidad());
            existingJuego.setGenero(juegoDTO.getGenero());
            existingJuego.setIdioma(juegoDTO.getIdioma());
            existingJuego.setDescripcion(juegoDTO.getDescripcion());
            existingJuego.setUbicacion(juegoDTO.getUbicacion());
            existingJuego.setRecomendadoDosJugadores(juegoDTO.getRecomendadoDosJugadores());
            existingJuego.setActivo(juegoDTO.getActivo());
            return new JuegoDTO(juegoRepository.save(existingJuego));
        });
    }

    public boolean delete(Integer id) {
        if (juegoRepository.existsById(id)) {
            juegoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}