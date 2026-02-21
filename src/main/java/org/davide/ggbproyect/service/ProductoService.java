package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Producto;
import org.davide.ggbproyect.models.ProductoDTO;
import org.davide.ggbproyect.repository.LineasComandaRepository;
import org.davide.ggbproyect.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final LineasComandaRepository lineasComandaRepository;

    public ProductoService(ProductoRepository productoRepository,
                           LineasComandaRepository lineasComandaRepository) {
        this.productoRepository = productoRepository;
        this.lineasComandaRepository = lineasComandaRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductoDTO> getAll(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(ProductoDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductoDTO getById(Integer id) {
        return productoRepository.findById(id)
                .map(ProductoDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Producto con id " + id + " no encontrado"));
    }

    public ProductoDTO create(ProductoDTO productoDTO) {
        if (productoDTO.getPrecio() != null && productoDTO.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio del producto no puede ser negativo");
        }
        if (productoRepository.existsByNombre(productoDTO.getNombre())) {
            throw new IllegalArgumentException("El nombre del producto esta duplicado ");
        }
        Producto producto = productoDTO.toEntity();
        return new ProductoDTO(productoRepository.save(producto));
    }

    public ProductoDTO update(Integer id, ProductoDTO productoDTO) {
        Producto existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con id " + id + " no encontrado"));
        if (productoRepository.existsByNombreAndIdNot(productoDTO.getNombre(), id)) {
            throw new IllegalArgumentException("El nombre del Producto esta duplicado ");
        }
        existingProducto.setNombre(productoDTO.getNombre());
        existingProducto.setDescripcion(productoDTO.getDescripcion());
        existingProducto.setCategoria(productoDTO.getCategoria());
        existingProducto.setPrecio(productoDTO.getPrecio());
        existingProducto.setActivo(productoDTO.getActivo());
        existingProducto.setTipoIva(productoDTO.getTipoIva());

        return new ProductoDTO(productoRepository.save(existingProducto));
    }

    @Transactional(readOnly = true)
    public Page<ProductoDTO> filter(String nombre, String categoria, Boolean activo, Pageable pageable) {
        return productoRepository.filter(nombre, categoria, activo, pageable)
                .map(ProductoDTO::new);
    }

    public boolean delete(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con id " + id + " no encontrado"));
        try {
            productoRepository.deleteById(id);
            productoRepository.flush();
            return true;
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            producto.setActivo(false);
            productoRepository.save(producto);
            return false;
        }
    }
}