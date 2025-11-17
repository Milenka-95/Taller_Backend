package com.taller.modiesel.service;
import com.taller.modiesel.model.DetalleVenta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface DetalleVentaService {
    List<DetalleVenta> listarTodos();
    Optional<DetalleVenta> obtenerPorId(Long id);
    DetalleVenta guardar(DetalleVenta detalleVenta);
    DetalleVenta actualizar(Long id, DetalleVenta detalleVenta);
    void eliminar(Long id);
}
