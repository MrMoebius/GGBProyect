package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Comanda;
import org.davide.ggbproyect.models.LudotecaSesiones;
import org.davide.ggbproyect.models.LudotecaSesionesDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.repository.ComandaRepository;
import org.davide.ggbproyect.repository.LudotecaSesionesRepository;
import org.davide.ggbproyect.repository.SesionesMesaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LudotecaSesionesService {

    private final LudotecaSesionesRepository ludotecaSesionesRepository;
    private final SesionesMesaRepository sesionesMesaRepository;
    private final ComandaRepository comandaRepository;

    public LudotecaSesionesService(LudotecaSesionesRepository ludotecaSesionesRepository,
                                   SesionesMesaRepository sesionesMesaRepository,
                                   ComandaRepository comandaRepository) {
        this.ludotecaSesionesRepository = ludotecaSesionesRepository;
        this.sesionesMesaRepository = sesionesMesaRepository;
        this.comandaRepository = comandaRepository;
    }

    public Page<LudotecaSesionesDTO> getAll(Pageable pageable) {
        return ludotecaSesionesRepository.findAll(pageable)
                .map(LudotecaSesionesDTO::new);
    }

    public LudotecaSesionesDTO getById(Integer id) {
        return ludotecaSesionesRepository.findById(id)
                .map(LudotecaSesionesDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Sesion de ludoteca con id " + id + " no encontrada"));
    }

    public LudotecaSesionesDTO create(LudotecaSesionesDTO ludotecaSesionesDTO) {
        LudotecaSesiones ludotecaSesiones = ludotecaSesionesDTO.toEntity();
        if (ludotecaSesionesDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(ludotecaSesionesDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + ludotecaSesionesDTO.getIdSesion() + " no encontrada"));
            ludotecaSesiones.setIdSesion(sesion);
        }
        if (ludotecaSesionesDTO.getIdComandaLudoteca() != null) {
            Comanda comanda = comandaRepository.findById(ludotecaSesionesDTO.getIdComandaLudoteca())
                    .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + ludotecaSesionesDTO.getIdComandaLudoteca() + " no encontrada"));
            ludotecaSesiones.setIdComandaLudoteca(comanda);
        }
        return new LudotecaSesionesDTO(ludotecaSesionesRepository.save(ludotecaSesiones));
    }

    public LudotecaSesionesDTO update(Integer id, LudotecaSesionesDTO ludotecaSesionesDTO) {
        LudotecaSesiones existingSesion = ludotecaSesionesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sesion de ludoteca con id " + id + " no encontrada"));
        if (ludotecaSesionesDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(ludotecaSesionesDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + ludotecaSesionesDTO.getIdSesion() + " no encontrada"));
            existingSesion.setIdSesion(sesion);
        }
        existingSesion.setNumAdultos(ludotecaSesionesDTO.getNumAdultos());
        existingSesion.setNumNinos613(ludotecaSesionesDTO.getNumNinos613());
        existingSesion.setNumNinos05(ludotecaSesionesDTO.getNumNinos05());
        existingSesion.setImporteTotal(ludotecaSesionesDTO.getImporteTotal());
        if (ludotecaSesionesDTO.getIdComandaLudoteca() != null) {
            Comanda comanda = comandaRepository.findById(ludotecaSesionesDTO.getIdComandaLudoteca())
                    .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + ludotecaSesionesDTO.getIdComandaLudoteca() + " no encontrada"));
            existingSesion.setIdComandaLudoteca(comanda);
        } else {
            existingSesion.setIdComandaLudoteca(null);
        }
        existingSesion.setFechaCalculo(ludotecaSesionesDTO.getFechaCalculo());
        existingSesion.setNotas(ludotecaSesionesDTO.getNotas());
        return new LudotecaSesionesDTO(ludotecaSesionesRepository.save(existingSesion));
    }

    public List<LudotecaSesionesDTO> filter(Integer idSesion) {
        return ludotecaSesionesRepository.filter(idSesion)
                .stream()
                .map(LudotecaSesionesDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!ludotecaSesionesRepository.existsById(id)) {
            throw new EntityNotFoundException("Sesion de ludoteca con id " + id + " no encontrada");
        }
        ludotecaSesionesRepository.deleteById(id);
    }
}