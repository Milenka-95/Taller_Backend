package com.taller.modiesel.service;
import com.taller.modiesel.model.Venta;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface VentaService {
    List<Venta> listarVentas();
    Venta obtenerVentaPorId(Long id);
    Venta registrarVenta(Venta venta);
    Venta actualizarVenta(Long id, Venta venta);
    void eliminarVenta(Long id);
}
