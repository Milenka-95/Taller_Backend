package com.taller.modiesel.service.impl;

import com.taller.modiesel.model.Inventario;
import com.taller.modiesel.repository.InventarioRepository;
import com.taller.modiesel.service.InventarioService;
import com.taller.modiesel.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.taller.modiesel.dto.VentaDTO;
import com.taller.modiesel.dto.DetalleVentaDTO;
import java.util.List;
import java.util.Optional;


@Service("inventarioService")
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioServiceImpl(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public List<Inventario> listarInventario() {
        return inventarioRepository.findAll();
    }

    @Override
    public Inventario obtenerPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public Inventario registrarProducto(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @Override
    public Inventario actualizarInventario(Long id, Inventario inventario) {
        Inventario existente = obtenerPorId(id);
        existente.setNombre(inventario.getNombre());
        existente.setCantidad(inventario.getCantidad());
        existente.setPrecio(inventario.getPrecio());
        existente.setTipoMovimiento(inventario.getTipoMovimiento());
        existente.setDescripcionMovimiento(inventario.getDescripcionMovimiento());
        return inventarioRepository.save(existente);
    }

    public void actualizarStockDesdeVenta(VentaDTO venta) {
        for (DetalleVentaDTO detalle : venta.getDetalles()) {
            Long productoId = detalle.getProductoId();
            int cantidadVendida = detalle.getCantidad();
            Inventario inventario = obtenerPorId(productoId);
            int nuevoStock = inventario.getCantidad() - cantidadVendida;
            if (nuevoStock < 0) {
                throw new IllegalArgumentException("Stock insuficiente para la venta");
            }
            inventario.setCantidad(nuevoStock);
            inventario.setTipoMovimiento("SALIDA");
            inventario.setDescripcionMovimiento("Venta de producto ID: " + productoId + ", cantidad: " + cantidadVendida);
            inventarioRepository.save(inventario);
        }
    }



    @Override
    public void eliminarInventario(Long id) {
        inventarioRepository.deleteById(id);
    }
}

