package com.taller.modiesel.service.impl;

import com.taller.modiesel.model.Imagen;
import com.taller.modiesel.repository.ImagenRepository;
import com.taller.modiesel.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImagenServiceImpl implements ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    @Override
    public Imagen guardarImagen(Imagen imagen) {
        return imagenRepository.save(imagen);
    }

    @Override
    public List<Imagen> listarImagenes() {
        return imagenRepository.findAll();
    }

    @Override
    public List<Imagen> listarPorTipo(String tipo) {
        return imagenRepository.findByTipo(tipo);
    }

    @Override
    public Imagen obtenerPorId(Long id) {
        return imagenRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarImagen(Long id) {
        imagenRepository.deleteById(id);
    }
}
