package org.davide.ggbproyect.service;

import lombok.RequiredArgsConstructor;
import org.davide.ggbproyect.models.Cliente;
import org.davide.ggbproyect.models.ClienteDTO;
import org.davide.ggbproyect.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<ClienteDTO> getAll(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(ClienteDTO::new);
    }

    public ClienteDTO getById(Integer id) {
        return clienteRepository.findById(id)
                .map(ClienteDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Cliente con id " + id + " no encontrado"));
    }

    public ClienteDTO create(ClienteDTO clienteDTO) {
        Cliente cliente = clienteDTO.toEntity();
        if (clienteDTO.getPassword() != null) {
            cliente.setPassword(passwordEncoder.encode(clienteDTO.getPassword()));
        }
        return new ClienteDTO(clienteRepository.save(cliente));
    }

    public ClienteDTO update(Integer id, ClienteDTO clienteDTO) {
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente con id " + id + " no encontrado"));
        existingCliente.setNombre(clienteDTO.getNombre());
        existingCliente.setEmail(clienteDTO.getEmail());
        existingCliente.setTelefono(clienteDTO.getTelefono());
        existingCliente.setFechaAlta(clienteDTO.getFechaAlta());
        existingCliente.setNotas(clienteDTO.getNotas());
        if (clienteDTO.getPassword() != null && !clienteDTO.getPassword().isBlank()) {
            existingCliente.setPassword(passwordEncoder.encode(clienteDTO.getPassword()));
        }
        return new ClienteDTO(clienteRepository.save(existingCliente));
    }

    public List<ClienteDTO> filter(String nombre, String email, String telefono) {
        return clienteRepository.filter(nombre, email, telefono)
                .stream()
                .map(ClienteDTO::new)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente con id " + id + " no encontrado");
        }
        clienteRepository.deleteById(id);
    }
}
