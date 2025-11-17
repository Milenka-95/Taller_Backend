package com.taller.modiesel.service.impl;

import com.taller.modiesel.model.Inventario;
import com.taller.modiesel.model.Venta;
import com.taller.modiesel.repository.VentaRepository;
import com.taller.modiesel.service.VentaService;
import com.taller.modiesel.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));
    }

    @Autowired
    private org.apache.camel.ProducerTemplate producerTemplate;


    public Venta registrarVenta(Venta venta) {
        Venta nueva = ventaRepository.save(venta);
        producerTemplate.sendBody("direct:procesarVenta", nueva);
        return nueva;
    }


    @Override
    public Venta actualizarVenta(Long id, Venta venta) {
        Venta existente = obtenerVentaPorId(id);
        existente.setCliente(venta.getCliente());
        existente.setFecha(venta.getFecha());
        existente.setTotal(venta.getTotal());
        return ventaRepository.save(existente);
    }

    @Override
    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }
}
