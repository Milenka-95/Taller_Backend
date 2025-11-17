package com.taller.modiesel.service;

import com.taller.modiesel.model.Proveedor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface ProveedorService {
    List<Proveedor> listarTodos();
    Optional<Proveedor> obtenerPorId(Long id);
    Proveedor guardar(Proveedor proveedor);
    Proveedor actualizar(Long id, Proveedor proveedor);
    void eliminar(Long id);
}
