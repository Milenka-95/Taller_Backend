package com.taller.modiesel.repository;

import com.taller.modiesel.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    // Ejemplo opcional: List<Vehiculo> findByPropietarioId(Long clienteId);
}
