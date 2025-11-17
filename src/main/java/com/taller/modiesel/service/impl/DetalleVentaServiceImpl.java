package com.taller.modiesel.service.impl;

import com.taller.modiesel.model.DetalleVenta;
import com.taller.modiesel.repository.DetalleVentaRepository;
import com.taller.modiesel.service.DetalleVentaService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaServiceImpl(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public Optional<DetalleVenta> obtenerPorId(Long id) {
        return detalleVentaRepository.findById(id);
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public DetalleVenta actualizar(Long id, DetalleVenta detalleVenta) {
        if (detalleVentaRepository.existsById(id)) {
            detalleVenta.setId(id);
            return detalleVentaRepository.save(detalleVenta);
        }
        return null;
    }

    @Override
    public void eliminar(Long id) {
        detalleVentaRepository.deleteById(id);
    }
}
