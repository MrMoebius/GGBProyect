package org.davide.ggbproyect.service;

import org.davide.ggbproyect.repository.ClienteRepository;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;

    public ClienteService(ClienteRepository clienteRepository,
                          EmpleadoRepository empleadoRepository) {
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;

    }

}


/*
*   @Service
    @RequiredArgsConstructor // <--- Esto genera el constructor automáticamente para campos 'final'
    public class ClienteService {
        private final ClienteRepository clienteRepository;
        // No hace falta escribir el constructor a mano.
}*/