package com.taller.modiesel.repository;

import com.taller.modiesel.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}