package com.taller.modiesel.repository;

import com.taller.modiesel.model.Repuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepuestoRepository extends JpaRepository<Repuesto, Long> {
    // Ejemplo opcional: List<Repuesto> findByVehiculoMarca(String marca);
}
