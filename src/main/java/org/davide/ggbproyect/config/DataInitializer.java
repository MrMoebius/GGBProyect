package org.davide.ggbproyect.config;

import org.davide.ggbproyect.models.Empleado;
import org.davide.ggbproyect.models.RolesEmpleado;
import org.davide.ggbproyect.repository.EmpleadoRepository;
import org.davide.ggbproyect.repository.RolesEmpleadoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EmpleadoRepository empleadoRepository;
    private final RolesEmpleadoRepository rolesEmpleadoRepository;
    private final PasswordEncoder passwordEncoder;

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
            RolesEmpleado adminRole = rolesEmpleadoRepository.findAll().stream()
                    .filter(r -> r.getNombreRol().equals("ADMIN"))
                    .findFirst()
                    .orElseThrow();

            Empleado admin = new Empleado();
            admin.setNombre("Admin User");
            admin.setEmail("admin@ggbproyect.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setIdRol(adminRole);
            admin.setFechaIngreso(LocalDate.now());
            admin.setEstado("ACTIVO");
            
            empleadoRepository.save(admin);
            System.out.println("Admin user created: admin@ggbproyect.com / admin123");
        }
    }
}
