package com.taller.modiesel.service.impl;

import com.taller.modiesel.model.Repuesto;
import com.taller.modiesel.repository.RepuestoRepository;
import com.taller.modiesel.service.RepuestoService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RepuestoServiceImpl implements RepuestoService {

    private final RepuestoRepository repuestoRepository;

    public RepuestoServiceImpl(RepuestoRepository repuestoRepository) {
        this.repuestoRepository = repuestoRepository;
    }

    @Override
    public List<Repuesto> listarTodos() {
        return repuestoRepository.findAll();
    }

    @Override
    public Optional<Repuesto> obtenerPorId(Long id) {
        return repuestoRepository.findById(id);
    }

    @Override
    public Repuesto guardar(Repuesto repuesto) {
        return repuestoRepository.save(repuesto);
    }

    @Override
    public Repuesto actualizar(Long id, Repuesto repuesto) {
        if (repuestoRepository.existsById(id)) {
            repuesto.setId(id);
            return repuestoRepository.save(repuesto);
        }
        return null;
    }

    @Override
    public void eliminar(Long id) {
        repuestoRepository.deleteById(id);
    }
}
