package com.taller.modiesel.repository;
import com.taller.modiesel.model.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByRuc(String ruc);
}
