package com.taller.modiesel.service;

import com.taller.modiesel.model.Vehiculo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface VehiculoService {
    List<Vehiculo> listarTodos();
    Optional<Vehiculo> obtenerPorId(Long id);
    Vehiculo guardar(Vehiculo vehiculo);
    Vehiculo actualizar(Long id, Vehiculo vehiculo);
    void eliminar(Long id);
}
