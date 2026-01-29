package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Comanda;
import org.davide.ggbproyect.models.LudotecaSesiones;
import org.davide.ggbproyect.models.LudotecaSesionesDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.repository.LudotecaSesionesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LudotecaSesionesService {

    private final LudotecaSesionesRepository ludotecaSesionesRepository;

    public LudotecaSesionesService(LudotecaSesionesRepository ludotecaSesionesRepository) {
        this.ludotecaSesionesRepository = ludotecaSesionesRepository;
    }

    public List<LudotecaSesionesDTO> getAll() {
        return ludotecaSesionesRepository.findAll().stream()
                .map(LudotecaSesionesDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<LudotecaSesionesDTO> getById(Integer id) {
        return ludotecaSesionesRepository.findById(id)
                .map(LudotecaSesionesDTO::new);
    }

    public LudotecaSesionesDTO create(LudotecaSesionesDTO ludotecaSesionesDTO) {
        LudotecaSesiones ludotecaSesiones = ludotecaSesionesDTO.toEntity();
        return new LudotecaSesionesDTO(ludotecaSesionesRepository.save(ludotecaSesiones));
    }

    public Optional<LudotecaSesionesDTO> update(Integer id, LudotecaSesionesDTO ludotecaSesionesDTO) {
        return ludotecaSesionesRepository.findById(id).map(existingSesion -> {
            if (ludotecaSesionesDTO.getIdSesion() != null) {
                SesionesMesa sesion = new SesionesMesa();
                sesion.setId(ludotecaSesionesDTO.getIdSesion());
                existingSesion.setIdSesion(sesion);
            }
            existingSesion.setNumAdultos(ludotecaSesionesDTO.getNumAdultos());
            existingSesion.setNumNinos613(ludotecaSesionesDTO.getNumNinos613());
            existingSesion.setNumNinos05(ludotecaSesionesDTO.getNumNinos05());
            existingSesion.setImporteTotal(ludotecaSesionesDTO.getImporteTotal());
            if (ludotecaSesionesDTO.getIdComandaLudoteca() != null) {
                Comanda comanda = new Comanda();
                comanda.setId(ludotecaSesionesDTO.getIdComandaLudoteca());
                existingSesion.setIdComandaLudoteca(comanda);
            } else {
                existingSesion.setIdComandaLudoteca(null);
            }
            existingSesion.setFechaCalculo(ludotecaSesionesDTO.getFechaCalculo());
            existingSesion.setNotas(ludotecaSesionesDTO.getNotas());
            return new LudotecaSesionesDTO(ludotecaSesionesRepository.save(existingSesion));
        });
    }

    public boolean delete(Integer id) {
        if (ludotecaSesionesRepository.existsById(id)) {
            ludotecaSesionesRepository.deleteById(id);
            return true;
        }
        return false;
    }
}