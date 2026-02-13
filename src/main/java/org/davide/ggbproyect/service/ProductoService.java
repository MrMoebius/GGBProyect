package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Producto;
import org.davide.ggbproyect.models.ProductoDTO;
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
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
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
        Producto producto = productoDTO.toEntity();
        List<Producto> listProducto= productoRepository.findAll();

        for (Producto producto1 : listProducto) {
            if (producto1.getNombre().equals(productoDTO.getNombre())) {
                throw new IllegalArgumentException("El nombre del producto esta duplicado ");
            }
        }
        return new ProductoDTO(productoRepository.save(producto));
    }

    public ProductoDTO update(Integer id, ProductoDTO productoDTO) {
        Producto existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con id " + id + " no encontrado"));
        existingProducto.setNombre(productoDTO.getNombre());
        existingProducto.setDescripcion(productoDTO.getDescripcion());
        existingProducto.setCategoria(productoDTO.getCategoria());
        existingProducto.setPrecio(productoDTO.getPrecio());
        existingProducto.setActivo(productoDTO.getActivo());

        List<Producto> listProducto = productoRepository.findAll();

        for (Producto producto1 : listProducto) {
            if (producto1.getNombre().equals(productoDTO.getNombre()) && !producto1.getId().equals(id)) {
                throw new IllegalArgumentException("El nombre del Producto esta duplicado ");
            }
        }



        return new ProductoDTO(productoRepository.save(existingProducto));
    }

    @Transactional(readOnly = true)
    public Page<ProductoDTO> filter(String nombre, String categoria, Boolean activo, Pageable pageable) {
        return productoRepository.filter(nombre, categoria, activo, pageable)
                .map(ProductoDTO::new);
    }

    public void delete(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto con id " + id + " no encontrado");
        }
        productoRepository.deleteById(id);
    }
}