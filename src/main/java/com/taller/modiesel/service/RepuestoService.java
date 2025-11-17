package com.taller.modiesel.service;

import com.taller.modiesel.model.Repuesto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface RepuestoService {
    List<Repuesto> listarTodos();
    Optional<Repuesto> obtenerPorId(Long id);
    Repuesto guardar(Repuesto repuesto);
    Repuesto actualizar(Long id, Repuesto repuesto);
    void eliminar(Long id);
}
