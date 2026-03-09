package org.davide.ggbproyect.config;

import org.davide.ggbproyect.models.Empleado;
import org.davide.ggbproyect.models.RolesEmpleado;
import org.davide.ggbproyect.models.enums.EstadoEmpleado;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.davide.ggbproyect.repository.RolesEmpleadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final EmpleadoRepository empleadoRepository;
    private final RolesEmpleadoRepository rolesEmpleadoRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_PASSWORD:#{null}}")
    private String adminPassword;

    public DataInitializer(EmpleadoRepository empleadoRepository,
                           RolesEmpleadoRepository rolesEmpleadoRepository,
                           PasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.rolesEmpleadoRepository = rolesEmpleadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if admin role exists, if not create it
        if (rolesEmpleadoRepository.count() == 0) {
            RolesEmpleado adminRole = new RolesEmpleado();
            adminRole.setNombreRol("ADMIN");
            rolesEmpleadoRepository.save(adminRole);

            RolesEmpleado staffRole = new RolesEmpleado();
            staffRole.setNombreRol("EMPLEADO");
            rolesEmpleadoRepository.save(staffRole);
        }

        // Check if admin user exists, if not create it
        if (empleadoRepository.findByEmail("admin@ggbproyect.com").isEmpty()) {
            if (adminPassword == null || adminPassword.isBlank()) {
                log.warn("ADMIN_PASSWORD not set. Skipping admin user creation.");
                return;
            }

            RolesEmpleado adminRole = rolesEmpleadoRepository.findByNombreRol("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("Rol ADMIN no encontrado"));

            Empleado admin = new Empleado();
            admin.setNombre("Admin User");
            admin.setEmail("admin@ggbproyect.com");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setIdRol(adminRole);
            admin.setFechaIngreso(LocalDate.now());
            admin.setEstado(EstadoEmpleado.ACTIVO);

            empleadoRepository.save(admin);
            log.info("Admin user created successfully.");
        }
    }
}
