package com.taller.modiesel.service.impl;

import com.taller.modiesel.model.Factura;
import com.taller.modiesel.repository.FacturaRepository;
import com.taller.modiesel.service.FacturaService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service("facturaService")
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;

    public FacturaServiceImpl(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    @Override
    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }

    @Override
    public Optional<Factura> obtenerPorId(Long id) {
        return facturaRepository.findById(id);
    }

    @Override
    public Factura guardar(Factura factura) {
        return facturaRepository.save(factura);
    }

    @Override
    public Factura actualizar(Long id, Factura factura) {
        if (facturaRepository.existsById(id)) {
            factura.setId(id);
            return facturaRepository.save(factura);
        }
        return null;
    }

    @Override
    public void eliminar(Long id) {
        facturaRepository.deleteById(id);
    }
}
