package com.taller.modiesel.repository;
import com.taller.modiesel.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    // Ejemplo opcional: Optional<Factura> findByNumero(String numero);
}
