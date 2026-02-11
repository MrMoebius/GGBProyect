package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.PagosMesa;
import org.davide.ggbproyect.models.PagosMesaDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.enums.EstadoPago;
import org.davide.ggbproyect.models.enums.MetodoPago;
import org.davide.ggbproyect.repository.PagosMesaRepository;
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
public class PagosMesaService {

    private final PagosMesaRepository pagosMesaRepository;
    private final SesionesMesaRepository sesionesMesaRepository;

    public PagosMesaService(PagosMesaRepository pagosMesaRepository,
                            SesionesMesaRepository sesionesMesaRepository) {
        this.pagosMesaRepository = pagosMesaRepository;
        this.sesionesMesaRepository = sesionesMesaRepository;
    }

    @Transactional(readOnly = true)
    public Page<PagosMesaDTO> getAll(Pageable pageable) {
        return pagosMesaRepository.findAll(pageable)
                .map(PagosMesaDTO::new);
    }

    @Transactional(readOnly = true)
    public PagosMesaDTO getById(Integer id) {
        return pagosMesaRepository.findById(id)
                .map(PagosMesaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Pago de mesa con id " + id + " no encontrado"));
    }

    public PagosMesaDTO create(PagosMesaDTO pagosMesaDTO) {
        PagosMesa pagosMesa = pagosMesaDTO.toEntity();
        if (pagosMesaDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(pagosMesaDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + pagosMesaDTO.getIdSesion() + " no encontrada"));
            pagosMesa.setIdSesion(sesion);
        }
        return new PagosMesaDTO(pagosMesaRepository.save(pagosMesa));
    }

    public PagosMesaDTO update(Integer id, PagosMesaDTO pagosMesaDTO) {
        PagosMesa existingPago = pagosMesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago de mesa con id " + id + " no encontrado"));
        if (pagosMesaDTO.getIdSesion() != null) {
            SesionesMesa sesion = sesionesMesaRepository.findById(pagosMesaDTO.getIdSesion())
                    .orElseThrow(() -> new EntityNotFoundException("Sesion de mesa con id " + pagosMesaDTO.getIdSesion() + " no encontrada"));
            existingPago.setIdSesion(sesion);
        }
        existingPago.setFechaHora(pagosMesaDTO.getFechaHora());
        existingPago.setImporte(pagosMesaDTO.getImporte());
        if (pagosMesaDTO.getMetodoPago() != null) {
            try {
                existingPago.setMetodoPago(MetodoPago.valueOf(pagosMesaDTO.getMetodoPago()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de metodo de pago invalido: " + pagosMesaDTO.getMetodoPago());
            }
        }
        if (pagosMesaDTO.getEstado() != null) {
            try {
                existingPago.setEstado(EstadoPago.valueOf(pagosMesaDTO.getEstado()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de estado invalido: " + pagosMesaDTO.getEstado());
            }
        }
        return new PagosMesaDTO(pagosMesaRepository.save(existingPago));
    }

    @Transactional(readOnly = true)
    public Page<PagosMesaDTO> filter(Integer idSesion, String metodoPago, String estado, Pageable pageable) {
        return pagosMesaRepository.filter(idSesion, metodoPago, estado, pageable)
                .map(PagosMesaDTO::new);
    }

    public void delete(Integer id) {
        if (!pagosMesaRepository.existsById(id)) {
            throw new EntityNotFoundException("Pago de mesa con id " + id + " no encontrado");
        }
        pagosMesaRepository.deleteById(id);
    }
}