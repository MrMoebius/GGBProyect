package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Comanda;
import org.davide.ggbproyect.models.LudotecaSesiones;
import org.davide.ggbproyect.models.LudotecaSesionesDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.TarifasLudoteca;
import org.davide.ggbproyect.repository.ComandaRepository;
import org.davide.ggbproyect.repository.LudotecaSesionesRepository;
import org.davide.ggbproyect.repository.SesionesMesaRepository;
import org.davide.ggbproyect.repository.TarifasLudotecaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class LudotecaSesionesService {

    private final LudotecaSesionesRepository ludotecaSesionesRepository;
    private final SesionesMesaRepository sesionesMesaRepository;
    private final ComandaRepository comandaRepository;
    private final TarifasLudotecaRepository tarifasLudotecaRepository;

    public LudotecaSesionesService(LudotecaSesionesRepository ludotecaSesionesRepository,
                                   SesionesMesaRepository sesionesMesaRepository,
                                   ComandaRepository comandaRepository,
                                   TarifasLudotecaRepository tarifasLudotecaRepository) {
        this.ludotecaSesionesRepository = ludotecaSesionesRepository;
        this.sesionesMesaRepository = sesionesMesaRepository;
        this.comandaRepository = comandaRepository;
        this.tarifasLudotecaRepository = tarifasLudotecaRepository;
    }

    @Transactional(readOnly = true)
    public Page<LudotecaSesionesDTO> getAll(Pageable pageable) {
        return ludotecaSesionesRepository.findAll(pageable)
                .map(LudotecaSesionesDTO::new);
    }

    @Transactional(readOnly = true)
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
        if (ludotecaSesionesDTO.getIdComandaLudoteca() != null) {
            Comanda comanda = comandaRepository.findById(ludotecaSesionesDTO.getIdComandaLudoteca())
                    .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + ludotecaSesionesDTO.getIdComandaLudoteca() + " no encontrada"));
            existingSesion.setIdComandaLudoteca(comanda);
        } else {
            existingSesion.setIdComandaLudoteca(null);
        }
        existingSesion.setNotas(ludotecaSesionesDTO.getNotas());
        calcularImporteTarifa(existingSesion);
        return new LudotecaSesionesDTO(ludotecaSesionesRepository.save(existingSesion));
    }

    @Transactional(readOnly = true)
    public Page<LudotecaSesionesDTO> filter(Integer idSesion, Pageable pageable) {
        return ludotecaSesionesRepository.filter(idSesion, pageable)
                .map(LudotecaSesionesDTO::new);
    }

    private void calcularImporteTarifa(LudotecaSesiones ludoteca) {
        List<TarifasLudoteca> tarifas = tarifasLudotecaRepository.findByActivoTrue();
        BigDecimal total = BigDecimal.ZERO;

        int numAdultos = ludoteca.getNumAdultos() != null ? ludoteca.getNumAdultos() : 0;
        int numNinos613 = ludoteca.getNumNinos613() != null ? ludoteca.getNumNinos613() : 0;
        int numNinos05 = ludoteca.getNumNinos05() != null ? ludoteca.getNumNinos05() : 0;

        for (TarifasLudoteca tarifa : tarifas) {
            int edadMin = tarifa.getEdadMin();
            int edadMax = tarifa.getEdadMax();
            if (edadMin >= 18) {
                total = total.add(tarifa.getPrecio().multiply(BigDecimal.valueOf(numAdultos)));
            } else if (edadMin >= 6) {
                total = total.add(tarifa.getPrecio().multiply(BigDecimal.valueOf(numNinos613)));
            } else {
                total = total.add(tarifa.getPrecio().multiply(BigDecimal.valueOf(numNinos05)));
            }
        }

        ludoteca.setImporteTotal(total);
        ludoteca.setFechaCalculo(Instant.now());
    }

    public void delete(Integer id) {
        if (!ludotecaSesionesRepository.existsById(id)) {
            throw new EntityNotFoundException("Sesion de ludoteca con id " + id + " no encontrada");
        }
        ludotecaSesionesRepository.deleteById(id);
    }
}