package com.taller.modiesel.service;
import com.taller.modiesel.model.Factura;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface FacturaService {
    List<Factura> listarTodas();
    Optional<Factura> obtenerPorId(Long id);
    Factura guardar(Factura factura);
    Factura actualizar(Long id, Factura factura);
    void eliminar(Long id);
}
