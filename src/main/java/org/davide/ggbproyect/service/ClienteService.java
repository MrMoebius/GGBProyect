package org.davide.ggbproyect.service;

import lombok.RequiredArgsConstructor;
import org.davide.ggbproyect.models.Cliente;
import org.davide.ggbproyect.models.ClienteDTO;
import org.davide.ggbproyect.repository.ClienteRepository;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;

    public List<ClienteDTO> getAll() {
        return clienteRepository.findAll().stream()
                .map(ClienteDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ClienteDTO> getById(Integer id) {
        return clienteRepository.findById(id)
                .map(ClienteDTO::new);
    }

    public ClienteDTO create(ClienteDTO clienteDTO) {
        Cliente cliente = clienteDTO.toEntity();
        return new ClienteDTO(clienteRepository.save(cliente));
    }

    public Optional<ClienteDTO> update(Integer id, ClienteDTO clienteDTO) {
        return clienteRepository.findById(id).map(existingCliente -> {
            existingCliente.setNombre(clienteDTO.getNombre());
            existingCliente.setEmail(clienteDTO.getEmail());
            existingCliente.setTelefono(clienteDTO.getTelefono());
            existingCliente.setFechaAlta(clienteDTO.getFechaAlta());
            existingCliente.setNotas(clienteDTO.getNotas());
            // No actualizamos la contraseña
            return new ClienteDTO(clienteRepository.save(existingCliente));
        });
    }

    public boolean delete(Integer id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}