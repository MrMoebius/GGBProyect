package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Comanda;
import org.davide.ggbproyect.models.LineasComanda;
import org.davide.ggbproyect.models.LineasComandaDTO;
import org.davide.ggbproyect.models.Producto;
import org.davide.ggbproyect.repository.ComandaRepository;
import org.davide.ggbproyect.repository.LineasComandaRepository;
import org.davide.ggbproyect.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineasComandaService {

    private final LineasComandaRepository lineasComandaRepository;
    private final ComandaRepository comandaRepository;
    private final ProductoRepository productoRepository;

    public LineasComandaService(LineasComandaRepository lineasComandaRepository,
                                ComandaRepository comandaRepository,
                                ProductoRepository productoRepository) {
        this.lineasComandaRepository = lineasComandaRepository;
        this.comandaRepository = comandaRepository;
        this.productoRepository = productoRepository;
    }

    public Page<LineasComandaDTO> getAll(Pageable pageable) {
        return lineasComandaRepository.findAll(pageable)
                .map(LineasComandaDTO::new);
    }

    public LineasComandaDTO getById(Integer id) {
        return lineasComandaRepository.findById(id)
                .map(LineasComandaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Linea de comanda con id " + id + " no encontrada"));
    }

    public LineasComandaDTO create(LineasComandaDTO lineasComandaDTO) {
        LineasComanda lineasComanda = lineasComandaDTO.toEntity();
        if (lineasComandaDTO.getIdComanda() != null) {
            Comanda comanda = comandaRepository.findById(lineasComandaDTO.getIdComanda())
                    .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + lineasComandaDTO.getIdComanda() + " no encontrada"));
            lineasComanda.setIdComanda(comanda);
        }
        if (lineasComandaDTO.getIdProducto() != null) {
            Producto producto = productoRepository.findById(lineasComandaDTO.getIdProducto())
                    .orElseThrow(() -> new EntityNotFoundException("Producto con id " + lineasComandaDTO.getIdProducto() + " no encontrado"));
            lineasComanda.setIdProducto(producto);
        }
        return new LineasComandaDTO(lineasComandaRepository.save(lineasComanda));
    }

    public LineasComandaDTO update(Integer id, LineasComandaDTO lineasComandaDTO) {
        LineasComanda existingLinea = lineasComandaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Linea de comanda con id " + id + " no encontrada"));
        if (lineasComandaDTO.getIdComanda() != null) {
            Comanda comanda = comandaRepository.findById(lineasComandaDTO.getIdComanda())
                    .orElseThrow(() -> new EntityNotFoundException("Comanda con id " + lineasComandaDTO.getIdComanda() + " no encontrada"));
            existingLinea.setIdComanda(comanda);
        }
        if (lineasComandaDTO.getIdProducto() != null) {
            Producto producto = productoRepository.findById(lineasComandaDTO.getIdProducto())
                    .orElseThrow(() -> new EntityNotFoundException("Producto con id " + lineasComandaDTO.getIdProducto() + " no encontrado"));
            existingLinea.setIdProducto(producto);
        }
        existingLinea.setCantidad(lineasComandaDTO.getCantidad());
        existingLinea.setPrecioUnitarioHistorico(lineasComandaDTO.getPrecioUnitarioHistorico());
        existingLinea.setEstadoPreparacion(lineasComandaDTO.getEstadoPreparacion());
        existingLinea.setNotasChef(lineasComandaDTO.getNotasChef());
        return new LineasComandaDTO(lineasComandaRepository.save(existingLinea));
    }

    public Page<LineasComandaDTO> filter(Integer idComanda, Integer idProducto, Pageable pageable) {
        return lineasComandaRepository.filter(idComanda, idProducto, pageable)
                .map(LineasComandaDTO::new);
    }

    public void delete(Integer id) {
        if (!lineasComandaRepository.existsById(id)) {
            throw new EntityNotFoundException("Linea de comanda con id " + id + " no encontrada");
        }
        lineasComandaRepository.deleteById(id);
    }
}