package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.Producto;
import org.davide.ggbproyect.models.ProductoDTO;
import org.davide.ggbproyect.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductoDTO> getAll() {
        return productoRepository.findAll().stream()
                .map(ProductoDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ProductoDTO> getById(Integer id) {
        return productoRepository.findById(id)
                .map(ProductoDTO::new);
    }

    public ProductoDTO create(ProductoDTO productoDTO) {
        Producto producto = productoDTO.toEntity();
        return new ProductoDTO(productoRepository.save(producto));
    }

    public Optional<ProductoDTO> update(Integer id, ProductoDTO productoDTO) {
        return productoRepository.findById(id).map(existingProducto -> {
            existingProducto.setNombre(productoDTO.getNombre());
            existingProducto.setDescripcion(productoDTO.getDescripcion());
            existingProducto.setCategoria(productoDTO.getCategoria());
            existingProducto.setPrecio(productoDTO.getPrecio());
            existingProducto.setActivo(productoDTO.getActivo());
            return new ProductoDTO(productoRepository.save(existingProducto));
        });
    }

    public boolean delete(Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}