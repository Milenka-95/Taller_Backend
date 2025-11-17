package com.taller.modiesel.service.impl;

import com.taller.modiesel.model.Cliente;
import com.taller.modiesel.model.Vehiculo;
import com.taller.modiesel.repository.ClienteRepository;
import com.taller.modiesel.repository.VehiculoRepository;
import com.taller.modiesel.service.VehiculoService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }

    @Override
    public Optional<Vehiculo> obtenerPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    @Override
    public Vehiculo guardar(Vehiculo vehiculo) {

        return vehiculoRepository.save(vehiculo);
    }

    @Override
    public Vehiculo actualizar(Long id, Vehiculo vehiculo) {
        if (vehiculoRepository.existsById(id)) {
            vehiculo.setId(id);
            return vehiculoRepository.save(vehiculo);
        }
        return null;
    }

    @Override
    public void eliminar(Long id) {
        vehiculoRepository.deleteById(id);
    }
}
