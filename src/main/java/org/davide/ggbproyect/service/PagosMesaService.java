package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.PagosMesa;
import org.davide.ggbproyect.models.PagosMesaDTO;
import org.davide.ggbproyect.models.SesionesMesa;
import org.davide.ggbproyect.models.enums.EstadoPago;
import org.davide.ggbproyect.models.enums.MetodoPago;
import org.davide.ggbproyect.repository.PagosMesaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public PagosMesaDTO getById(Integer id) {
        return pagosMesaRepository.findById(id)
                .map(PagosMesaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Pago de mesa con id " + id + " no encontrado"));
    }

    public PagosMesaDTO create(PagosMesaDTO pagosMesaDTO) {
        PagosMesa pagosMesa = pagosMesaDTO.toEntity();
        return new PagosMesaDTO(pagosMesaRepository.save(pagosMesa));
    }

    public PagosMesaDTO update(Integer id, PagosMesaDTO pagosMesaDTO) {
        PagosMesa existingPago = pagosMesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago de mesa con id " + id + " no encontrado"));
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

    public List<PagosMesaDTO> filter(Integer idSesion, String metodoPago, String estado) {
        return pagosMesaRepository.filter(idSesion, metodoPago, estado)
                .stream()
                .map(PagosMesaDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!pagosMesaRepository.existsById(id)) {
            throw new EntityNotFoundException("Pago de mesa con id " + id + " no encontrado");
        }
        pagosMesaRepository.deleteById(id);
    }
}