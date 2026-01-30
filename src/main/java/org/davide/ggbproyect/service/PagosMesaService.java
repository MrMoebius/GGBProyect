package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.PagosMesa;
import org.davide.ggbproyect.models.PagosMesaDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.enums.EstadoPago;
import org.davide.ggbproyect.models.enums.MetodoPago;
import org.davide.ggbproyect.repository.PagosMesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagosMesaService {

    private final PagosMesaRepository pagosMesaRepository;

    public PagosMesaService(PagosMesaRepository pagosMesaRepository) {
        this.pagosMesaRepository = pagosMesaRepository;
    }

    public List<PagosMesaDTO> getAll() {
        return pagosMesaRepository.findAll().stream()
                .map(PagosMesaDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<PagosMesaDTO> getById(Integer id) {
        return pagosMesaRepository.findById(id)
                .map(PagosMesaDTO::new);
    }

    public PagosMesaDTO create(PagosMesaDTO pagosMesaDTO) {
        PagosMesa pagosMesa = pagosMesaDTO.toEntity();
        return new PagosMesaDTO(pagosMesaRepository.save(pagosMesa));
    }

    public Optional<PagosMesaDTO> update(Integer id, PagosMesaDTO pagosMesaDTO) {
        return pagosMesaRepository.findById(id).map(existingPago -> {
            if (pagosMesaDTO.getIdSesion() != null) {
                SesionesMesa sesion = new SesionesMesa();
                sesion.setId(pagosMesaDTO.getIdSesion());
                existingPago.setIdSesion(sesion);
            }
            existingPago.setFechaHora(pagosMesaDTO.getFechaHora());
            existingPago.setImporte(pagosMesaDTO.getImporte());
            if (pagosMesaDTO.getMetodoPago() != null) {
                try {
                    existingPago.setMetodoPago(MetodoPago.valueOf(pagosMesaDTO.getMetodoPago()));
                } catch (IllegalArgumentException e) {
                    // Handle invalid enum
                }
            }
            if (pagosMesaDTO.getEstado() != null) {
                try {
                    existingPago.setEstado(EstadoPago.valueOf(pagosMesaDTO.getEstado()));
                } catch (IllegalArgumentException e) {
                    // Handle invalid enum
                }
            }
            return new PagosMesaDTO(pagosMesaRepository.save(existingPago));
        });
    }

    public boolean delete(Integer id) {
        if (pagosMesaRepository.existsById(id)) {
            pagosMesaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}