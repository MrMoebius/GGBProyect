package org.davide.ggbproyect.repository;

import org.davide.ggbproyect.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
