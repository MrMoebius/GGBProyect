package org.davide.ggbproyect.security;

import org.davide.ggbproyect.models.Cliente;
import org.davide.ggbproyect.models.Empleado;
import org.davide.ggbproyect.repository.ClienteRepository;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmpleadoRepository empleadoRepository;
    private final ClienteRepository clienteRepository;

    public CustomUserDetailsService(EmpleadoRepository empleadoRepository, ClienteRepository clienteRepository) {
        this.empleadoRepository = empleadoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try to find employee first
        Optional<Empleado> empleado = empleadoRepository.findByEmail(email);
        if (empleado.isPresent()) {
            Set<GrantedAuthority> authorities = Collections.singleton(
                    new SimpleGrantedAuthority("ROLE_" + empleado.get().getIdRol().getNombreRol())
            );
            return new User(empleado.get().getEmail(), empleado.get().getPassword(), authorities);
        }

        // If not employee, try client
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);
        if (cliente.isPresent()) {
            Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_CLIENTE"));
            return new User(cliente.get().getEmail(), cliente.get().getPassword(), authorities);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
