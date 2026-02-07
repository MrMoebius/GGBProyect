package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Comanda;
import org.davide.ggbproyect.models.LineasComanda;
import org.davide.ggbproyect.models.LineasComandaDTO;
import org.davide.ggbproyect.models.Producto;
import org.davide.ggbproyect.repository.LineasComandaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineasComandaService {

    private final LineasComandaRepository lineasComandaRepository;

    public LineasComandaService(LineasComandaRepository lineasComandaRepository) {
        this.lineasComandaRepository = lineasComandaRepository;
    }

    public List<LineasComandaDTO> getAll() {
        return lineasComandaRepository.findAll().stream()
                .map(LineasComandaDTO::new)
                .collect(Collectors.toList());
    }

    public LineasComandaDTO getById(Integer id) {
        return lineasComandaRepository.findById(id)
                .map(LineasComandaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Linea de comanda con id " + id + " no encontrada"));
    }

    public LineasComandaDTO create(LineasComandaDTO lineasComandaDTO) {
        LineasComanda lineasComanda = lineasComandaDTO.toEntity();
        return new LineasComandaDTO(lineasComandaRepository.save(lineasComanda));
    }

    public LineasComandaDTO update(Integer id, LineasComandaDTO lineasComandaDTO) {
        LineasComanda existingLinea = lineasComandaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Linea de comanda con id " + id + " no encontrada"));
        if (lineasComandaDTO.getIdComanda() != null) {
            Comanda comanda = new Comanda();
            comanda.setId(lineasComandaDTO.getIdComanda());
            existingLinea.setIdComanda(comanda);
        }
        if (lineasComandaDTO.getIdProducto() != null) {
            Producto producto = new Producto();
            producto.setId(lineasComandaDTO.getIdProducto());
            existingLinea.setIdProducto(producto);
        }
        existingLinea.setCantidad(lineasComandaDTO.getCantidad());
        existingLinea.setPrecioUnitarioHistorico(lineasComandaDTO.getPrecioUnitarioHistorico());
        existingLinea.setEstadoPreparacion(lineasComandaDTO.getEstadoPreparacion());
        existingLinea.setNotasChef(lineasComandaDTO.getNotasChef());
        return new LineasComandaDTO(lineasComandaRepository.save(existingLinea));
    }

    public List<LineasComandaDTO> filter(Integer idComanda, Integer idProducto) {
        return lineasComandaRepository.filter(idComanda, idProducto)
                .stream()
                .map(LineasComandaDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!lineasComandaRepository.existsById(id)) {
            throw new EntityNotFoundException("Linea de comanda con id " + id + " no encontrada");
        }
        lineasComandaRepository.deleteById(id);
    }
}