package com.taller.modiesel.repository;

import com.taller.modiesel.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    // Ejemplo opcional: buscar proveedor por RUC
    // Optional<Proveedor> findByRuc(String ruc);
}
