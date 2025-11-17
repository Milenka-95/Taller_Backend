package com.taller.modiesel.repository;

import com.taller.modiesel.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    // Ejemplo de consulta personalizada opcional:
    // List<Inventario> findByNombreContainingIgnoreCase(String nombre);
}
